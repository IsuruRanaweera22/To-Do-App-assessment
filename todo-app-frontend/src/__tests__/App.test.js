import { render, screen } from '@testing-library/react';
import App from '../App';

jest.mock('../TodoLayout', () => () => <div data-testid="todo-layout">Mocked TodoLayout</div>);

test('renders TodoLayout component', () => {
  render(<App />);
  expect(screen.getByTestId('todo-layout')).toBeInTheDocument();
});
