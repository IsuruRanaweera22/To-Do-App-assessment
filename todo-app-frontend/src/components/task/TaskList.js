import React from 'react';
import styles from '../../styles/TodoLayout.module.css';

const TaskList = ({ tasks, markAsDone, filterDate, setFilterDate, clearFilter }) => {
  return (
    <>
      <div className={styles.dateSection}>
        <label htmlFor="filterDate">Filter Tasks: </label>
        <input
          type="date"
          id="filterDate"
          value={filterDate}
          onChange={(e) => setFilterDate(e.target.value)}
        />
        {filterDate && (
          <button onClick={clearFilter} className={styles.Button}>
            Clear Filters
          </button>
        )}
      </div>
      <div className={styles.resultTodo}>
        {tasks.map((task) => (
          <ul className={styles.results} key={task.taskId}>
            <li className={styles.details}>
              <div>
                <h1>{task.title}</h1>
                <p>{task.description}</p>
              </div>
              <div>
                <p>Due on: {task.dueDate || '-'}</p>
              </div>
            </li>
            <button className={styles.Button} type="button" onClick={() => markAsDone(task.taskId)}>
              Done
            </button>
          </ul>
        ))}
      </div>
    </>
  );
};

export default TaskList;
