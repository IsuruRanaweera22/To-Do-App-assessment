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
  axios.post.mockResolvedValue({ data: { message: 'Task added successfully' } });
  axios.put.mockResolvedValue({ status: 200 });
  axios.get.mockResolvedValue({
    data: [
      { taskId: 1, title: 'Task 1', description: 'Task 1 Description', dueDate: '2025-01-01' },
      { taskId: 2, title: 'Task 2', description: 'Task 2 Description', dueDate: '2025-01-02' }
    ]
  });
  
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
  
    fireEvent.click(screen.getByRole('button', { name: /submit/i })); // Click instead of submit
  
    await waitFor(() => {
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/save'), // Match "/save" endpoint
        expect.objectContaining({
          title: 'New Task',
          description: 'Task Description',
          dueDate: '',
        }),
        expect.objectContaining({
          headers: { 'Content-Type': 'application/json' },
        })
      );
    });
  
    expect(axios.post).toHaveBeenCalledTimes(1); // Ensure it was only called once
  });
  
  test('calls fetchTasks on mount', async () => {
    await act(async () => {
      render(<TodoLayout />);
    });
    await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(1));
  });

});
