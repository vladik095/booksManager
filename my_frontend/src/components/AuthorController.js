import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { Container, Paper, Button, Typography } from '@mui/material';

export function AuthorController() {
    const paperStyle = { padding: "20px 20px", width: 600, margin: "20px auto" };
    const [authors, setAuthors] = useState([]);
    const [keyword, setKeyword] = useState("");
    const [error, setError] = useState(null);

    const fetchAuthorsByKeyword = () => {
        if (!keyword) {
            setError("Please enter a keyword");
            return;
        }

        fetch(`http://localhost:8080/authors/search?keyword=${keyword}`)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error("Authors not found");
                }
            })
            .then(authors => {
                setAuthors(authors);
                setError(null);
            })
            .catch(error => {
                setAuthors([]);
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
            <Paper elevation={3} style={{ ...paperStyle, marginTop: '50px' }}>
                <Box
                    component="form"
                    sx={{
                        '& > :not(style)': { m: 1 },
                    }}
                    noValidate
                    autoComplete="off"
                >
                    <Typography variant="h4" gutterBottom>Find Author</Typography>
                    <TextField
                        id="outlined-basic"
                        label="Enter a keyword"
                        variant="outlined"
                        value={keyword}
                        onChange={(e) => setKeyword(e.target.value)}
                    />
                    <br />
                    <Button
                        variant="contained"
                        onClick={fetchAuthorsByKeyword}
                    >
                        Find
                    </Button>
                    {error && <Typography variant="body2" color="error">{error}</Typography>}
                </Box>
            </Paper>
            {authors.length === 0 && <Typography variant="body2">No authors found</Typography>}
            {authors.map(author => (
                <Paper elevation={6} style={{ margin: "10px", padding: "15px", textAlign: "left" }} key={author.id}>
                    <Typography variant="h5">ID: {author.id}</Typography>
                    <Typography variant="h5">Name: {author.name}</Typography>
                    <Typography variant="h5">Books:</Typography>
                    <ul>
                        {author.books.map(book => (
                            <li key={book.id}><Typography variant="h5">{book.title}</Typography></li>
                        ))}
                    </ul>
                    <Button
                        variant="contained"
                        color="error"
                        onClick={() => handleDeleteAuthor(author.id)}
                    >
                        Delete
                    </Button>
                </Paper>
            ))}
        </Container>
    );
}    