import React from 'react';
import Box from '@mui/material/Box';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import { Link } from 'react-router-dom';
import { AuthorController } from '../components/AuthorController';

const appStyle = {
    background: 'linear-gradient(to right, #1a1c20, #2d2f36, #36383d)', 
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
};

export function Author() {
    const linkStyle = {
        marginRight: '20px',
        color: 'white',
        textDecoration: 'none',
        padding: '5px 10px',
        fontWeight: 'bold',
    };

    return (
        <div className='App' style={appStyle}>
            <Box sx={{ flexGrow: 1 }}>
            <AppBar position="fixed" style={{ top: 0 }} sx={{ backgroundColor: '#3f51b5' }}>
                    <Toolbar>
                        <Link style={linkStyle} to="/book">Book</Link>
                        <div style={{ flexGrow: 1 }} />
                        <Link style={{ textAlign: 'right', color: 'white', textDecoration: 'none', padding: '5px 5px', fontWeight: 'bold' }} to="/">
                            Home
                        </Link>
                    </Toolbar>
                </AppBar>
                <AuthorController />
            </Box>
        </div>
    );
}
