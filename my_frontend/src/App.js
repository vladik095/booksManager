import './App.css';
import {
  BrowserRouter,
  Routes,
  Route,
} from 'react-router-dom';

import { MainPage } from './pages/MainPage';
import { Author } from './pages/Author';
import { Book } from './pages/Book'; // Импортируем компонент Book

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/author" element={<Author />} />
        <Route path="/book" element={<Book />} /> {/* Добавляем маршрут для компонента Book */}
      </Routes>
    </BrowserRouter>
  );
}
