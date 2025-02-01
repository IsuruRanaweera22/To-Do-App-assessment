import { saveTask } from '../../services/TaskService.js';
import { SubmitBtn } from '../button/button.js';
import React, { useState } from 'react';
import Swal from 'sweetalert2';
import styles from '../../styles/TodoLayout.module.css';

const TaskForm = ({ fetchTasks, setLoading, loading }) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [dueDate, setDueDate] = useState('');
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!title.trim() || !description.trim()) {
      Swal.fire({
        icon: 'warning',
        title: 'Validation Error',
        text: 'Please fill out all required fields!',
      });
      return;
    }
    setLoading(true);
    try {
      const taskData = { title, description, dueDate };
      const success = await saveTask(taskData);
      if (success) {
        Swal.fire({
          icon: 'success',
          title: 'Task Added',
          text: 'Your task has been added successfully!',
        });
        setTitle('');
        setDescription('');
        setDueDate('');
        fetchTasks();
      } else {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Failed to add the task!',
        });
      }
    } catch (error) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'An error occurred while adding the task!',
      });
    } finally {
      setLoading(false);
    }
  };
  return (
    <form onSubmit={handleSubmit}>
        <div className={styles.inputList}>
          <div className={styles.taskName}>
            <label htmlFor="name">Task Title: </label>
            <input
              placeholder="Enter title.."
              type="text"
              id="name"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </div>
          <div className={styles.description}>
            <label htmlFor="description">Description: </label>
            <textarea
              placeholder="Enter description.."
              rows={8}
              cols={50}
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>
          <div className={styles.dueDate}>
            <label htmlFor="date">Due Date: </label>
            <input
              type="date"
              id="date"
              value={dueDate}
              onChange={(e) => setDueDate(e.target.value)}
              min={new Date().toISOString().split('T')[0]}
            />
          </div>
          <div className={styles.button}>
            {loading ? 'Adding task...' : <SubmitBtn disabled={false} />}
          </div>
        </div>
      </form>
  );
};

export default TaskForm;



