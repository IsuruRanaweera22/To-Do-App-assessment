import axios from 'axios';
import Swal from 'sweetalert2'; 

const API_URL = process.env.REACT_APP_API_URL;
export const getAllTasks = async () => {
  try {
    const response = await axios.get(`${API_URL}/get-all-completed`, {
      params: {
        status: false,
        page: 0,
        size: 5,
      },
    });
    if (response?.data?.data) {
      return response.data.data;
    }
    throw new Error('Failed to fetch tasks');
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const saveTask = async (taskData) => {
    try {
      const response = await axios.post(`${API_URL}/save`, taskData, {
        headers: { 'Content-Type': 'application/json' },
      });
      return response.data;
    } catch (error) {
      console.error('Failed to save task:', error);
      throw error;
    }
};

export const markTaskAsDone = async (id) => {
  try {
    await axios.put(`${API_URL}/${id}/complete`);
    Swal.fire({
      icon: 'success',
      title: 'Task Completed',
      text: 'Done',
    });
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Failed to mark the task as done!',
    });
  }
};
