import React, { useState, useEffect } from 'react';
import styles from './TodoLayout.module.css'; // Make sure to replace with your actual CSS module
import { SubmitBtn } from "./components/button.js";
import Swal from 'sweetalert2'; // Import SweetAlert2
import axios from 'axios'; // Use import instead of require

const TodoLayout = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [allTasks, setAllTasks] = useState([]); // Ensure allTasks is initialized as an empty array
  const [filterDate, setFilterDate] = useState(''); // State for filter date
  const [loading, setLoading] = useState(false); // State for loading spinner

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const response = await fetch(`http://localhost:8081/api/v1/task/get-all-completed/${false}`);
      if (!response || !response.ok) {
        throw new Error("Failed to fetch tasks");
      }
      const result = await response.json();
      if (Array.isArray(result.data)) {
        setAllTasks(result.data);
      }
    } catch (error) {
      console.error('Failed to fetch tasks:', error);
      setAllTasks([]); // Set to an empty array in case of an error
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validation logic
    if (!title.trim() || !description.trim()) {
      Swal.fire({
        icon: 'warning',
        title: 'Validation Error',
        text: 'Please fill out all required fields!',
      });
      return;
    }

    setLoading(true); // Set loading to true when starting the submission

    const todoData = {
      title,
      description,
      dueDate,
    };

    try {
      const response = await fetch('http://localhost:8081/api/v1/task/save', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(todoData),
      });

      setLoading(false); // Set loading to false after the request completes

      if (response.ok) {
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
      setLoading(false); // Ensure loading is false if an error occurs
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'An error occurred while adding the task!',
      });
    }
  };

  // Function to mark a task as done
  const markAsDone = async (id) => {
    try {
      await axios.put(`http://localhost:8081/api/v1/task/${id}/complete`);
      setAllTasks(allTasks.filter(task => task.id !== id));
      fetchTasks();
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

  // Filter tasks based on the selected filter date
  const filteredTasks = allTasks.filter(task => {
    if (!filterDate) return true; // If no filter date is selected, show all tasks
    return task.dueDate === filterDate;
  });

  // Clear the filter
  const clearFilter = () => {
    setFilterDate('');
  };

  return (
    <>
      <div className={styles.headingDiv}>
        <h1 className={`${styles.heading}`}>To-Do App</h1>
      </div>
      <div className={styles.wrapper}>
        <div className={styles.main}>
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
                {loading ? 'Adding task...' : <SubmitBtn disabled={loading} />}
              </div>
            </div>
          </form>
        </div>

        <div className={styles.resultcontainer}>
          <div className={styles.dateSection}>
            <label htmlFor="filterDate">Filter Tasks: </label>
            <input
              type="date"
              id="filterDate"
              value={filterDate}
              onChange={(e) => setFilterDate(e.target.value)}
            />
            {filterDate && (
              <button onClick={clearFilter} className={styles.Button}>Clear Filters</button>
            )}
          </div>
          <div className={styles.resultTodo}>
            {filteredTasks.map((task) => (
              <ul className={styles.results} key={task.taskId}>
                <li className={styles.details}>
                  <div>
                    <h1>{task.title}</h1>
                    <p>{task.description}</p>
                  </div>
                  <div>
                    <p>Due on: {task.dueDate || "-"}</p>
                  </div>
                </li>
                <button className={styles.Button} type="button" onClick={() => markAsDone(task.taskId)}>Done</button>
              </ul>
            ))}
          </div>
        </div>
      </div>
    </>
  );
};

export default TodoLayout;