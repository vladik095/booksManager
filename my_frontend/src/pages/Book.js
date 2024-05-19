import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import EditIcon from '@mui/icons-material/Edit';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import Autocomplete from '@mui/material/Autocomplete';
import Chip from '@mui/material/Chip';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import { Link } from 'react-router-dom';

export function Book({ currentAuthorId }) {
    const [books, setBooks] = useState([]);
    const [authors, setAuthors] = useState([]);
    const [tags, setTags] = useState([]);
    const [error, setError] = useState(null);
    const [hoveredCard, setHoveredCard] = useState(null);
    const [deleteId, setDeleteId] = useState(null);
    const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
    const [selectedBook, setSelectedBook] = useState(null);
    const [newAuthorName, setNewAuthorName] = useState("");
    const [showAddNewAuthorForm, setShowAddNewAuthorForm] = useState(false);
    const [showAddNewTagForm, setShowAddNewTagForm] = useState(false);
    const [newTagName, setNewTagName] = useState("");

    useEffect(() => {
        fetchAllBooks();
        fetchAllAuthors();
        fetchAllTags();
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

    const [showAddCardModal, setShowAddCardModal] = useState(false);
const [newCardData, setNewCardData] = useState({
    title: "",
    description: "",
    authorId: "",
    tagIds: [],
});

const resetNewCardData = () => {
    setNewCardData({
        title: "",
        description: "",
        authorId: "",
        tagIds: [],
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

    const fetchAllTags = () => {
        fetch("http://localhost:8080/api/tags")
            .then(response => response.json())
            .then(data => {
                setTags(data);
            })
            .catch(error => {
                console.error('Error fetching tags:', error);
                setError(error);
            });
    };

    const handleDelete = (id) => {
        setDeleteId(id);
        setShowDeleteConfirmation(true);
    };


    const handleTagDelete = (tagToDelete) => {
        const updatedTags = selectedBook.tags.filter((tag) => tag.id !== tagToDelete.id);
        setSelectedBook({ ...selectedBook, tags: updatedTags });
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
        const { id, authorId, tags } = updatedBook;
        const tagIds = tags.map(tag => tag.id); // Получаем массив идентификаторов тегов
    
        fetch(`http://localhost:8080/api/books/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: updatedBook.title,
                description: updatedBook.description,
                authorId: authorId,
                tagIds: tagIds, // Передаем массив идентификаторов тегов
            }),
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
    };

    const handleCancelEdit = () => {
        setSelectedBook(null);
    };

    const handleAddNewAuthor = () => {
        return fetch("http://localhost:8080/authors/save", {
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
                console.log('New author:', newAuthor);
                // Обновляем список авторов добавлением нового автора в состояние
                setAuthors(prevAuthors => [...prevAuthors, newAuthor]);
                
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

    const handleAddNewTagClick = () => {
        setShowAddNewTagForm(true);
    };

    const handleCancelAddTag = () => {
        setShowAddNewTagForm(false);
        setNewTagName("");
    };

    const handleAddNewTagFormSubmit = () => {
        fetch("http://localhost:8080/api/tags", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: newTagName }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to add new tag');
                }
                console.log('New tag added successfully');
                handleCancelAddTag(); // Сбрасываем форму добавления тега после успешного добавления
                fetchAllBooks();
                fetchAllTags();
                 // Обновляем список книг, возможно, он связан с тегами
            })
            
            .catch(error => {
                console.error('Error adding new tag:', error);
            });
    };

    const handleAddNewAuthorFormSubmit = () => {
        handleAddNewAuthor().then(() => {
            setShowAddNewAuthorForm(false);
            fetchAllAuthors();
        }).catch(error => console.error('Error adding new author:', error));
    };


    const handleAddCard = () => {
        fetch("http://localhost:8080/api/books/bulk", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify([newCardData]),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to add new card');
            }
            console.log('New card added successfully');
            setShowAddCardModal(false); // Закрываем модальное окно после успешного добавления
            fetchAllBooks(); // Обновляем список книг
        })
        .catch(error => {
            console.error('Error adding new card:', error);
        });
    };


    const linkStyle = { color: 'white', textDecoration: 'none', padding: '5px 5px', fontWeight: 'bold' };

    return (
        <div style={{ background: '#00FA', minHeight: '100vh', padding: '100px', position: 'relative' }}>

              <Box sx={{ flexGrow: 1 }}>
            <AppBar position="fixed" style={{ top: 0 }}>
                <Toolbar>
                    <Link style={linkStyle} to="/author">Author</Link>
                    <div style={{ flexGrow: 1 }} />
                    <Link style={linkStyle} to="/">Home</Link>
                </Toolbar>
            </AppBar>
        </Box>
            <Box>
                {error && <Typography variant="body2" color="error">{error}</Typography>}
                <Grid container spacing={4}>
                    {books.map((book, index) => (
                        <Grid item xs={12} sm={6} md={4} lg={3} key={book.id}>
                          <Paper
    elevation={3}
    style={{
        padding: "20px",
        height: "475px",
        width: "400px",
        position: "relative",
        border: hoveredCard === book.id ? "2px solid #3f51b5" : "5px solid #ccc",
        transition: "border-color 0.3s",
        borderRadius: "40px",
        backgroundColor: hoveredCard === book.id ? "#f0f0f0" : "#FFFFFF",
        margin: 'auto'
    }}
    onMouseEnter={() => setHoveredCard(book.id)}
    onMouseLeave={() => setHoveredCard(null)}
>
    <div style={{ position: "absolute", top: 0, right: 10 }}>
      
        <IconButton onClick={() => handleEdit(book)}>
            <EditIcon />
        </IconButton>
        <IconButton onClick={() => handleDelete(book.id)}>
            <CloseIcon />
        </IconButton>
    </div>
    <Typography variant="h4" style={{ fontSize: "3rem", color: "#FF6C00", textAlign: "center", textShadow: "2px 2px 0px black"}} gutterBottom>{book.title}</Typography>
    <Typography variant="body1" style={{ fontSize: "1.6rem", color: "#000000"}  }>Author: {book.authorName}</Typography>
    <Typography variant="body1" style={{ fontSize: "1.6rem" }}>Tags: {book.tags.map(tag => tag.name).join(', ')}</Typography>
    <Typography variant="body2" style={{ fontSize: "1.6rem", marginTop: "10px" }}>Description: {book.description}</Typography>
</Paper>

                        </Grid>
                    ))}
                    <Grid item xs={12} sm={6} md={4} lg={3}>
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
                                borderRadius: "40px",
                                backgroundColor: "#FFFFFF",
                                margin: 'auto'
                            }}
                        >
                          <Button onClick={() => setShowAddCardModal(true)}>
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
{showAddCardModal && (
    <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', background: '#fff', padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
        <Typography variant="h6">Add New Card</Typography>
        <TextField
            label="Title"
            fullWidth
            value={newCardData.title}
            onChange={(e) => setNewCardData({ ...newCardData, title: e.target.value })}
            style={{ marginBottom: '20px' }}
        />
        <TextField
            label="Description"
            fullWidth
            multiline
            rows={4}
            value={newCardData.description}
            onChange={(e) => setNewCardData({ ...newCardData, description: e.target.value })}
            style={{ marginBottom: '20px' }}
        />
<Autocomplete
    fullWidth
    options={authors}
    getOptionLabel={(author) => author.name}
    value={authors.find(author => author.id === newCardData.authorId) || null}
    onChange={(event, newValue) => {
        setNewCardData({ ...newCardData, authorId: newValue ? newValue.id : '' });
    }}
    renderInput={(params) => <TextField {...params} variant="outlined" label="Author" />}
    style={{ marginBottom: '20px' }}
/>


        <Autocomplete
            multiple
            id="tags-filled"
            options={tags}
            getOptionLabel={(tag) => tag.name}
            freeSolo
            renderTags={(value, getTagProps) =>
                value.map((option, index) => (
                    <Chip variant="outlined" label={option.name} {...getTagProps({ index })} />
                ))
            }
            renderInput={(params) => (
                <TextField {...params} variant="outlined" label="Tags" />
            )}
            onChange={(event, newValue) => {
                setNewCardData({ ...newCardData, tagIds: newValue.map(tag => tag.id) });
            }}
            style={{ marginBottom: '20px' }}
        />
    
    <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '10px' }}>
    <div>
        <Button onClick={handleAddNewAuthorClick} variant="contained" style={{ marginRight: '15px' }}>Add New Author</Button>
        <Button onClick={handleAddNewTagClick} variant="contained" style={{ marginRight: '50px' }}>Add New Tag</Button>
    </div>
    <div>
        <Button variant="contained" color="primary" onClick={handleAddCard} style={{ marginRight: '10px' }}>Save</Button>
        <Button variant="contained" onClick={() => { setShowAddCardModal(false); resetNewCardData(); }}>Cancel</Button>
    </div>
</div>

    </div>
)}

            {selectedBook && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', background: '#fff', padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' , width: '750px', height: '450px' }}>
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
                    <Autocomplete
                        multiple
                        id="tags-filled"
                        options={tags}
                        defaultValue={selectedBook.tags}
                        getOptionLabel={(tag) => tag.name}
                        freeSolo
                        renderTags={(value, getTagProps) =>
                            value.map((option, index) => (
                                <Chip variant="outlined" label={option.name} {...getTagProps({ index })} />
                            ))
                        }
                        renderInput={(params) => (
                            <TextField {...params} variant="outlined" label="Tags" />
                        )}
                        onChange={(event, newValue) => {
                            setSelectedBook({ ...selectedBook, tags: newValue });
                        }}
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
    <Button onClick={handleAddNewTagClick} variant="contained" style={{ marginLeft: '-250px' }}>Add New Tag</Button>
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
        label="New Author"
        fullWidth
        value={newAuthorName}
        onChange={(e) => setNewAuthorName(e.target.value)}
        style={{ marginBottom: '20px' }}
    />
    <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '10px' }}>
        <Button variant="contained" color="primary" onClick={handleAddNewAuthorFormSubmit} style={{ marginRight: '10px' }}>Save</Button>
        <Button variant="contained" onClick={handleCancelAddAuthor}>Cancel</Button>
    </div>
</div>
)}
{showAddNewTagForm && (
<div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', background: '#fff', padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
    <Typography variant="h6">Add New Tag</Typography>
    <TextField
        label="New Tag"
        fullWidth
        value={newTagName}
        onChange={(e) => setNewTagName(e.target.value)}
        style={{ marginBottom: '20px' }}
    />
    <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '10px' }}>
        <Button variant="contained" color="primary" onClick={handleAddNewTagFormSubmit} style={{ marginRight: '10px' }}>Save</Button>
        <Button variant="contained" onClick={handleCancelAddTag}>Cancel</Button>
    </div>
</div>
)}
</div>
);
}
