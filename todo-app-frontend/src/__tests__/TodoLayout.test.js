import React, {act} from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import TodoLayout from '../TodoLayout.js';
import Swal from 'sweetalert2';
import axios from 'axios';

jest.mock('sweetalert2', () => ({ fire: jest.fn() }));
jest.mock('axios');

// Reset all mocks before each test
beforeEach(() => {
  jest.clearAllMocks();
  global.fetch = jest.fn().mockResolvedValueOnce({
    ok: true,
    json: () => Promise.resolve({ data: [] }),
  });
});


// Mock API responses
axios.put.mockResolvedValue({
  data: { message: 'Task marked as complete' },
});

describe('TodoLayout Component', () => {
  test('renders TodoLayout component', async () => {
    await act(async () => {
      render(<TodoLayout />);
    });
    expect(screen.getByText(/To-Do App/i)).toBeInTheDocument();
  });

  test('validates form inputs before submission', async () => {
    await act(async () => {
      render(<TodoLayout />);
    });

    fireEvent.submit(screen.getByRole('button', { name: /submit/i }));

    await waitFor(() => {
      expect(Swal.fire).toHaveBeenCalledWith(
        expect.objectContaining({
          icon: 'warning',
          title: 'Validation Error',
        })
      );
    });
  });

  test('calls fetchTasks on mount', async () => {
    await act(async () => {
      render(<TodoLayout />);
    });
    await waitFor(() => expect(global.fetch).toHaveBeenCalledTimes(1));
  });

  test('adds a new task and calls API', async () => {
    await act(async () => {
      render(<TodoLayout />);
    });

    fireEvent.change(screen.getByPlaceholderText(/Enter title/i), {
      target: { value: 'New Task' },
    });

    fireEvent.change(screen.getByPlaceholderText(/Enter description/i), {
      target: { value: 'Task Description' },
    });

    fireEvent.submit(screen.getByRole('button', { name: /submit/i }));

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining('/api/v1/task/save'),
        expect.objectContaining({
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            title: 'New Task',
            description: 'Task Description',
            dueDate: '',
          }),
        })
      );
    });
    expect(fetch).toHaveBeenCalledTimes(3); // First on mount, second after adding a task
  });

  test('marks a task as done and calls API', async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: () =>
        Promise.resolve({
          data: [{ taskId: 1, title: 'Task 1', description: 'Task Description', dueDate: '2025-01-01' }],
        }),
    });

    await act(async () => {
      render(<TodoLayout />);
    });

    const doneButton = await screen.findByRole('button', { name: /done/i });
    fireEvent.click(doneButton);

    await waitFor(() => {
      expect(axios.put).toHaveBeenCalledWith(expect.stringContaining('/api/v1/task/1/complete'));
    });

    expect(fetch).toHaveBeenCalledTimes(2); // Fetch after marking as done
  });

  test('filters tasks correctly based on filter date', async () => {
    const tasks = [
      { taskId: 1, title: 'Task 1', description: 'Task 1 Description', dueDate: '2025-01-01' },
      { taskId: 2, title: 'Task 2', description: 'Task 2 Description', dueDate: '2025-01-02' },
    ];

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ data: tasks }),
    });

    await act(async () => {
      render(<TodoLayout />);
    });

    fireEvent.change(screen.getByLabelText(/Filter Tasks/i), {
      target: { value: '2025-01-01' },
    });

    await waitFor(() => {
      expect(screen.getByText('Task 1')).toBeInTheDocument();
      expect(screen.queryByText('Task 2')).toBeNull();
    });
  });

  test('clears filter when clear button is clicked', async () => {
    const tasks = [
      { taskId: 1, title: 'Task 1', description: 'Task 1 Description', dueDate: '2025-01-01' },
    ];

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ data: tasks }),
    });

    await act(async () => {
      render(<TodoLayout />);
    });

    fireEvent.change(screen.getByLabelText(/Filter Tasks/i), {
      target: { value: '2025-01-01' },
    });

    fireEvent.click(screen.getByText(/Clear Filters/i));

    await waitFor(() => {
      expect(screen.getByText('Task 1')).toBeInTheDocument(); // Verify that the task is visible again
    });
  });
});
