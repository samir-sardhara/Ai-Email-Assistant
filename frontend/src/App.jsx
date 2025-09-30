
import { useState } from 'react';
import './App.css'
import axios from "axios";



import {FormControl,Button,InputLabel,Select,MenuItem, Box, Container, TextField, Typography, CircularProgress } from '@mui/material';

function App() {
  
  const[emailContent,setEmailContent] = useState('')
  const[tone,steTone] = useState('')
  const[generatedReplay,setGeneratedReplay] = useState('')
  const[loading,setLoading] = useState(false)

  const handleSubmit = async () => {
    setLoading(true);
    try {
      const response = await axios.post("http://localhost:8080/api/email/generate",{
        emailContent,
        tone
      }
       
      )
      setGeneratedReplay(typeof response.data === 'string' ? 
        response.data : JSON.stringify(response.data)
      );
    } catch (error) {
      
    }finally{
      setLoading(false)
    }
  };

  return (
    <Container maxWidth="md" sx = {{py:4}}>
      <Typography variant='h3' component="h1" gutterBottom>
        Email Reply Generator 
      </Typography>
      <Box sx={{mx : 3}}>
        <TextField 
        fullWidth
        multiline
        rows={6}
        variant='outlined'
        label="Original Email Content"
        value={emailContent || ''}
        onChange={(e) => setEmailContent(e.target.value)}
        sx = {{mb :2}}
        />
        <FormControl fullWidth sx = {{mb :2}}>
            <InputLabel >Tone (Optional)</InputLabel>
            <Select
              
              value={tone || ''}
              label="Tone (Optional)"
              onChange={(e) => steTone(e.target.value)}
            >
              <MenuItem value= "">None</MenuItem>
              <MenuItem value="Professional">Professional</MenuItem>
              <MenuItem value="Casual">Casual</MenuItem>
              <MenuItem value="Friendly">Friendly</MenuItem>
            </Select>
        </FormControl>

        <Button variant="contained" 
        sx = {{mb :2}}
        onClick={handleSubmit}
        disabled = {!emailContent || loading}>
          {loading ? <CircularProgress size={24}/> : "Generate Reply"}
        </Button>

      
        
      </Box>
       <Box sx={{mx : 3}}>
        <TextField 
        fullWidth
        multiline
        rows={6}
        variant='outlined'
        value={generatedReplay || ''}
        inputProps={{readOnly :true }}
        sx = {{mb :2}}
        />
        <Button
        variant='outlined'
        onClick={() => navigator.clipboard.writeText(generatedReplay || "")}>
          Copy to Clicpboard

        </Button>
      
        </Box>

    </Container>
  )
}

export default App
