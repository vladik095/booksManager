import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import { Container, Paper, Typography } from '@mui/material';

export function BookController() {
    const paperStyle = { padding: "20px 20px", width: 600, margin: "20px auto" };
    const [books, setBooks] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchAllBooks();
    }, []);

    const fetchAllBooks = () => {
        fetch("http://localhost:8080/api/books")
            .then(response => response.json())
            .then(data => {
                setBooks(data);
            })
            .catch(error => {
                console.error('Error fetching books:', error);
                setError(error.message);
            });
    };

    return (
        <Container>
            <Paper elevation={3} style={paperStyle}>
                <Typography variant="h5" gutterBottom>Book List</Typography>
                {error && <Typography variant="body2" color="error">{error}</Typography>}
                {books.length === 0 && <Typography variant="body2">No books found</Typography>}
                {books.map(book => (
                    <Paper elevation={6} style={{ margin: "10px", padding: "15px", textAlign: "left" }} key={book.id}>
                        <Typography>ID: {book.id}</Typography>
                        <Typography>Title: {book.title}</Typography>
                        <Typography>Author ID: {book.author.id}</Typography>
                        <Typography>Author Name: {book.author.name}</Typography>
                        {/* Include other book details as needed */}
                    </Paper>
                ))}
            </Paper>
        </Container>
    );  
}
