import React, { useState, useEffect } from 'react';
import styles from './styles/TodoLayout.module.css';
import TaskForm from './components/task/TaskForm.js'
import {getAllTasks, markTaskAsDone } from './services/TaskService.js'

const TodoLayout = () => {
  const [allTasks, setAllTasks] = useState([]);
  const [filterDate, setFilterDate] = useState('');
  const [loading, setLoading] = useState(false); 

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const result = await getAllTasks();
      if (Array.isArray(result)) {
        setAllTasks(result);
      } else {
        console.error('Failed to fetch tasks');
        setAllTasks([]);
      }
    } catch (error) {
      console.error('Error fetching tasks:', error);
      setAllTasks([]);
    }
  };
  
  const markAsDone = async (id) => {
    await markTaskAsDone(id);
    setAllTasks(allTasks.filter(task => task.id !== id));
    fetchTasks();
  };
  
  const filteredTasks = allTasks.filter(task => {
    if (!filterDate) return true;
    return task.dueDate === filterDate;
  });

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
          <TaskForm fetchTasks={fetchTasks} setLoading={setLoading}  loading={loading}/>
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