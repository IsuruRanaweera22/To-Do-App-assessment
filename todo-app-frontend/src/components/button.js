import React from 'react';
import styles from '../components/button.module.css';

const SubmitBtn = ({disabled}) => {
  return (
    <button type="submit" className={styles.glowingbtn} aria-label="Submit">
      <span className={styles.glowingtxt}>
        SU<span className={styles.faultyletter}>BM</span>IT
      </span>
    </button>

  )
}

export {SubmitBtn};