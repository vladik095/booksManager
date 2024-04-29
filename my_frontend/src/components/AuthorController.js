import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { Container, Paper, Button, Typography } from '@mui/material';

export function AuthorController() {
    const paperStyle = { padding: "20px 20px", width: 600, margin: "20px auto" };
    const [authors, setAuthors] = useState([]);
    const [authorId, setAuthorId] = useState("");
    const [author, setAuthor] = useState(null);
    const [error, setError] = useState(null);

    const fetchAuthorById = () => {
        if (!authorId) {
            setError("Please enter author ID");
            return;
        }

        fetch(`http://localhost:8080/authors/get/${authorId}`)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error("Author not found");
                }
            })
            .then(author => {
                setAuthor(author);
                setError(null);
            })
            .catch(error => {
                setAuthor(null);
                setError(error.message);
            });
    };

    useEffect(() => {
        fetchAllAuthors();
    }, []);

    const fetchAllAuthors = () => {
        fetch("http://localhost:8080/authors/getAll")
            .then(response => response.json())
            .then(data => {
                setAuthors(data);
            })
            .catch(error => {
                console.error('Error fetching authors:', error);
            });
    };

    const handleDeleteAuthor = (id) => {
        fetch(`http://localhost:8080/authors/delete/${id}`, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    console.log("Author deleted successfully");
                    fetchAllAuthors(); // Refresh the author list
                } else {
                    console.error("Failed to delete author");
                }
            })
            .catch(error => {
                console.error("Error deleting author:", error);
            });
    };

    return (
        <Container>
            <Paper elevation={3} style={paperStyle}>
                <Box
                    component="form"
                    sx={{
                        '& > :not(style)': { m: 1 },
                    }}
                    noValidate
                    autoComplete="off"
                >
                    <Typography variant="h5" gutterBottom>Find Author by ID</Typography>
                    <TextField
                        id="outlined-basic"
                        label="Enter the author ID"
                        variant="outlined"
                        value={authorId}
                        onChange={(e) => setAuthorId(e.target.value)}
                    />
                    <br />
                    <Button
                        variant="contained"
                        onClick={fetchAuthorById}
                    >
                        Find
                    </Button>
                    {error && <Typography variant="body2" color="error">{error}</Typography>}
                </Box>
            </Paper>
            {author && (
                <Paper elevation={3} style={paperStyle}>
                    <Typography variant="h5" gutterBottom>Author Details</Typography>
                    <Typography>ID: {author.id}</Typography>
                    <Typography>Name: {author.name}</Typography>
                    <Typography>Books:</Typography>
                    <ul>
                        {author.books.map(book => (
                            <li key={book.id}>{book.title}</li>
                        ))}
                    </ul>
                </Paper>
            )}
            {authors.length === 0 && <Typography variant="body2">No authors found</Typography>}
            {authors.map(author => (
                <Paper elevation={6} style={{ margin: "10px", padding: "15px", textAlign: "left" }} key={author.id}>
                    <Typography>ID: {author.id}</Typography>
                    <Typography>Name: {author.name}</Typography>
                    <Typography>Books:</Typography>
                    <ul>
                        {author.books.map(book => (
                            <li key={book.id}>{book.title}</li>
                        ))}
                    </ul>
                    <Button
                        variant="contained"
                        color="secondary"
                        onClick={() => handleDeleteAuthor(author.id)}
                    >
                        Delete
                    </Button>
                </Paper>
            ))}
        </Container>
    );
}
