import React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import SearchIcon from '@mui/icons-material/Search';
import { Link } from 'react-router-dom';
//import { AuthorController } from '../components/AuthorController';

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
    fontSize: '20px', // увеличиваем размер шрифта для надписей внутри кнопок
};

const searchFieldStyles = {
    width: '60%',
    borderRadius: '50px',
    marginBottom: '20px',
    marginTop: '100px',
    textAlign: 'center', // Центрирование текста внутри поля
    position: 'relative', // задаем позицию для поисковой строки
};

const searchButtonStyles = {
    position: 'absolute', // задаем абсолютную позицию для кнопки-лупы
    right: '10px', // смещаем кнопку-лупу вправо
    top: '50%', // позиционируем кнопку-лупу по вертикали
    transform: 'translateY(-50%)', // корректируем позицию кнопки-лупы по вертикали
    backgroundColor: '#135592', // цвет тени лупы
    '&:hover': {
        backgroundColor: '#0e4569', // немного темнее при наведении
    },
    fontSize: '20px', // увеличиваем размер шрифта для надписей внутри кнопок
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

                <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', width: '80%', marginBottom: '50px' }}>
                    <TextField
                        id="outlined-basic"
                        label="Search"
                        variant="outlined"
                        style={searchFieldStyles}
                        placeholder="Fast search books by keyword"
                        InputProps={{
                            endAdornment: (
                                <Button variant="contained" color="primary" style={searchButtonStyles}>
                                    <SearchIcon />
                                </Button>
                            ),
                            style: { paddingRight: '50px' } // увеличиваем правый отступ для размещения кнопки-лупы
                        }}
                    />
                </Box>

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
