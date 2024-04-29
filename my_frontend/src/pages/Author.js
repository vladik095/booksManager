import React from 'react';
import Box from '@mui/material/Box';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import { Link } from 'react-router-dom';
import { AuthorController } from '../components/AuthorController';

export function Author() {
    const linkStyle = {
        marginRight: '20px', // Отступ справа между ссылками
        color: 'white', // Цвет текста
        textDecoration: 'none', // Убираем подчеркивание
        padding: '5px 10px', // Отступы вокруг текста
        fontWeight: 'bold', // Более жирный текст   
    };

    return (
        <div className='App'>
            <Box sx={{ flexGrow: 1 }}>
                <AppBar position="static">
                    <Toolbar>
                        <Link style={linkStyle} to="/author">Author</Link>
                        <Link style={linkStyle} to="/book">Book</Link>
                        <Link style={linkStyle} to="/publisher">Publisher</Link>
                        <Typography variant="h6" component="div" sx={{ flexGrow: 1, textAlign: 'center', marginRight: '200px' }}>
                            AuthorController
                        </Typography>
                        <Link style={{ textAlign: 'right', color: 'white', textDecoration: 'none', padding: '5px 5px', fontWeight: 'bold' }} to="/">
                            Home
                        </Link>
                    </Toolbar>
                </AppBar>
                <AuthorController /> {/* Using the AuthorController component */}
            </Box>
        </div>
    );
}
