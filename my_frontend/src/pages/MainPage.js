import React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import SearchIcon from '@mui/icons-material/Search';
import { Link } from 'react-router-dom';


const pageStyles = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100vh',
    backgroundImage: 'url(https://source.unsplash.com/random)',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    padding: '20px',
    textAlign: 'center',
    color: 'white',
};

const buttonStyles = {
    width: '200px',
    height: '200px',
    borderRadius: '20px',
    marginBottom: '20px',
    marginRight: '20px',
    backgroundColor: '#1976d2', // синий цвет кнопок
    '&:hover': {
        backgroundColor: '#135592', // немного темнее при наведении
    },
    fontSize: '20px',
};

const searchFieldStyles = {
    width: '60%',
    borderRadius: '50px',
    marginBottom: '20px',
    marginTop: '100px',
    textAlign: 'center', 
    position: 'relative', 
};

const searchButtonStyles = {
    position: 'absolute', 
    right: '10px', 
    top: '50%', 
    transform: 'translateY(-50%)', 
    backgroundColor: '#135592', 
    '&:hover': {
        backgroundColor: '#0e4569', 
    },
    fontSize: '20px', 
};

const welcomeTextStyle = {
    fontSize: '70px',
    marginBottom: '50px',
};

export function MainPage() {
    return (
        <div>
            <Box sx={pageStyles}>
                <Typography variant="h3" gutterBottom style={welcomeTextStyle}>
                    Welcome to Book Manager
                </Typography>

         
                <Box sx={{ display: 'flex', justifyContent: 'center', marginBottom: '50px' }}>
                    <Button variant="contained" color="primary" component={Link} to="/author" sx={buttonStyles}>
                        Authors
                    </Button>

                    <Button variant="contained" color="primary" component={Link} to="/book" sx={buttonStyles}>
                        Books
                    </Button>
                </Box>
            </Box>

            {/* <AuthorController /> */}
        </div>
    );
}
