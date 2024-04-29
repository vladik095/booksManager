import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import EditIcon from '@mui/icons-material/Edit';
import { Link } from 'react-router-dom';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';

export function Book({ currentAuthorId }) {
    const [books, setBooks] = useState([]);
    const [authors, setAuthors] = useState([]);
    const [error, setError] = useState(null);
    const [hoveredCard, setHoveredCard] = useState(null);
    const [deleteId, setDeleteId] = useState(null);
    const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
    const [selectedBook, setSelectedBook] = useState(null);
    const [newAuthorName, setNewAuthorName] = useState("");
    const [showAddNewAuthorForm, setShowAddNewAuthorForm] = useState(false);

    useEffect(() => {
        fetchAllBooks();
        fetchAllAuthors();
    }, []);

    const fetchAllBooks = () => {
        fetch("http://localhost:8080/api/books")
            .then(response => response.json())
            .then(data => {
                setBooks(data);
            })
            .catch(error => {
                console.error('Error fetching books:', error);
                setError(error);
            });
    };

    const fetchAllAuthors = () => {
        fetch("http://localhost:8080/authors/getAll")
            .then(response => response.json())
            .then(data => {
                setAuthors(data);
            })
            .catch(error => {
                console.error('Error fetching authors:', error);
                setError(error);
            });
    };

    const handleDelete = (id) => {
        setDeleteId(id);
        setShowDeleteConfirmation(true);
    };

    const handleConfirmDelete = () => {
        const idToDelete = deleteId;

        fetch(`http://localhost:8080/api/books/${idToDelete}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete book');
                }
                console.log('Book deleted successfully');
                setBooks(prevBooks => prevBooks.filter(book => book.id !== idToDelete));
                setDeleteId(null);
                setShowDeleteConfirmation(false);
            })
            .catch(error => {
                console.error('Error deleting book:', error);
                setDeleteId(null);
                setShowDeleteConfirmation(false);
            });
    };

    const handleCancelDelete = () => {
        setDeleteId(null);
        setShowDeleteConfirmation(false);
    };

    const handleEdit = (book) => {
        setSelectedBook({
            ...book,
            authorId: currentAuthorId,
        });
    };

    const handleSave = (updatedBook) => {
        const { id, authorId } = updatedBook;
        if (authorId === "__new__") {
            handleAddNewAuthor().then(() => {
                setSelectedBook(null);
                fetchAllBooks();
                fetchAllAuthors(); // обновляем список авторов после добавления нового
            }).catch(error => console.error('Error adding new author:', error));
        } else {
            fetch(`http://localhost:8080/api/books/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedBook),
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to update book');
                    }
                    console.log('Book updated successfully');
                    setSelectedBook(null);
                    fetchAllBooks();
                })
                .catch(error => {
                    console.error('Error updating book:', error);
                    setSelectedBook(null);
                });
        }
    };

    const handleCancelEdit = () => {
        setSelectedBook(null);
    };

    const handleAddNewAuthor = () => {
        return fetch("http://localhost:8080/authors/add", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: newAuthorName }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to add new author');
                }
                console.log('New author added successfully');
                setNewAuthorName("");
                return response.json(); // Получаем данные добавленного автора
            })
            .then(newAuthor => {
                // Обновляем список авторов
                setAuthors(prevAuthors => [...prevAuthors, newAuthor]);
                // Если данные нового автора получены, устанавливаем его id в selectedBook.authorId
                setSelectedBook(prevBook => ({ ...prevBook, authorId: newAuthor.id }));
            })
            .catch(error => {
                console.error('Error adding new author:', error);
            });
    };
    
    

    const handleCancelAddAuthor = () => {
        setNewAuthorName("");
        setShowAddNewAuthorForm(false);
    };

    const handleAddNewAuthorClick = () => {
        setShowAddNewAuthorForm(true);
    };

    const handleAddNewAuthorFormSubmit = () => {
        handleAddNewAuthor().then(() => {
            setShowAddNewAuthorForm(false);
            fetchAllAuthors();
        }).catch(error => console.error('Error adding new author:', error));
    };

    return (
        <div style={{ background: '#F5DEB3', minHeight: '100vh', padding: '20px', position: 'relative' }}>
            <Box>
                {error && <Typography variant="body2" color="error">{error}</Typography>}
                <Grid container spacing={4}>
                    {books.map((book, index) => (
                        <Grid item xs={12} sm={6} md={3} key={book.id}>
                            <Paper
                                elevation={3}
                                style={{
                                    padding: "20px",
                                    height: "475px",
                                    width: "400px",
                                    position: "relative",
                                    border: hoveredCard === book.id ? "2px solid #3f51b5" : "1px solid #ccc",
                                    transition: "border-color 0.3s",
                                    borderRadius: "8px",
                                    backgroundColor: hoveredCard === book.id ? "#f0f0f0" : "#D3D3D3",
                                    margin: 'auto'
                                }}
                                onMouseEnter={() => setHoveredCard(book.id)}
                                onMouseLeave={() => setHoveredCard(null)}
                            >
                                <IconButton
                                    onClick={() => handleDelete(book.id)}
                                    style={{ position: "absolute", top: 0, right: 0 }}
                                >
                                    <CloseIcon />
                                </IconButton>
                                <IconButton
                                    onClick={() => handleEdit(book)}
                                    style={{ position: "absolute", top: 0, left: 0 }}
                                >
                                    <EditIcon />
                                </IconButton>
                                <Typography variant="h4" gutterBottom>{book.title}</Typography>
                                <Typography variant="body1" style={{ fontSize: "1.4rem" }}>Author: {book.authorName}</Typography>
                                <Typography variant="body1" style={{ fontSize: "1.4rem" }}>Tags: {book.tags.map(tag => tag.name).join(', ')}</Typography>
                                <Typography variant="body2" style={{ fontSize: "1.2rem", marginTop: "10px" }}>{book.description}</Typography>
                            </Paper>
                        </Grid>
                    ))}
                    <Grid item xs={12} sm={6} md={3}>
                        <Paper
                            elevation={3}
                            style={{
                                padding: "20px",
                                height: "475px",
                                width: "400px",
                                display: "flex",
                                flexDirection: "column",
                                alignItems: "center",
                                justifyContent: "center",
                                borderRadius: "8px",
                                backgroundColor: "#D3D3D3",
                                margin: 'auto'
                            }}
                        >
                            <Button onClick={handleAddNewAuthorClick} style={{ textDecoration: "none" }}>
                                <AddCircleOutlineIcon fontSize="large" color="primary" />
                            </Button>
                        </Paper>
                    </Grid>
                </Grid>
            </Box>
            {showDeleteConfirmation && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', background: '#fff', padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
                    <Typography variant="h6">Are you sure you want to delete this book?</Typography>
                    <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'center' }}>
                        <Button variant="contained" color="primary" onClick={handleConfirmDelete} style={{ marginRight: '10px' }}>Yes</Button>
                        <Button variant="contained" onClick={handleCancelDelete}>No</Button>
                    </div>
                </div>
            )}
            {selectedBook && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', background: '#fff', padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
                    <Typography variant="h6">Edit Book</Typography>
                    <TextField
                        label="Title"
                        fullWidth
                        value={selectedBook.title}
                        onChange={(e) => setSelectedBook({ ...selectedBook, title: e.target.value })}
                        style={{ marginBottom: '20px' }}
                    />
                    <Select
                        label="Author"
                        fullWidth
                        value={selectedBook.authorId}
                        onChange={(e) => setSelectedBook({ ...selectedBook, authorId: e.target.value })}
                        style={{ marginBottom: '20px' }}
                    >
                        {authors.map(author => (
                            <MenuItem key={author.id} value={author.id}>{author.name}</MenuItem>
                        ))}
                    </Select>
                    <TextField
                        label="Tags"
                        fullWidth
                        value={selectedBook.tags.map(tag => tag.name).join(', ')}
                        onChange={(e) => setSelectedBook({ ...selectedBook, tags: e.target.value.split(',').map(tag => ({ name: tag.trim() })) })}
                        style={{ marginBottom: '20px' }}
                    />
                    <TextField
                        label="Description"
                        fullWidth
                        multiline
                        rows={4}
                        value={selectedBook.description}
                        onChange={(e) => setSelectedBook({ ...selectedBook, description: e.target.value })}
                        style={{ marginBottom: '20px' }}
                    />
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '10px' }}>
                        <Button onClick={handleAddNewAuthorClick} variant="contained">Add New Author</Button>
                        <div>
                            <Button variant="contained" color="primary" onClick={() => handleSave(selectedBook)} style={{ marginRight: '10px' }}>Save</Button>
                            <Button variant="contained" onClick={handleCancelEdit}>Cancel</Button>
                        </div>
                    </div>
                </div>
            )}
            {showAddNewAuthorForm && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', background: '#fff', padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
                    <Typography variant="h6">Add New Author</Typography>
                    <TextField
                        label="New Author Name"
                        fullWidth
                        value={newAuthorName}
                        onChange={(e) => setNewAuthorName(e.target.value)}
                        style={{ marginBottom: '20px' }}
                    />
                    <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <Button variant="contained" color="primary" onClick={handleAddNewAuthorFormSubmit} style={{ marginRight: '10px' }}>Add</Button>
                        <Button variant="contained" onClick={handleCancelAddAuthor}>Cancel</Button>
                    </div>
                </div>
            )}
        </div>
    );
}
