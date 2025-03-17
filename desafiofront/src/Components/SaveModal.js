import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  borderRadius: '12px', 
  boxShadow: '0px 4px 20px rgba(0, 0, 0, 0.2)', 
  p: 4,
};
export default function BasicModal({ data, open, onClose }) {
  if (!open) return null; 
  return (
    <Modal
      open={open}
      onClose={onClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <Typography 
          id="modal-modal-title" 
          variant="h6" 
          fontWeight="bold"
          sx={{ textAlign: 'left' }} 
        >
          {data.Title || "Confirmação"}
        </Typography>
        <Typography 
          id="modal-modal-description" 
          sx={{ mt: 2, textAlign: 'left' }} 
        >
          {data.Mensage || ""}
        </Typography>
        <Box sx={{ display: "flex", justifyContent: "flex-end", gap: 2, mt: 3 }}>
          <Button variant="outlined" onClick={onClose}>
           {data?"Fechar":"tentar novamente"} 
          </Button>
        </Box>
      </Box>
    </Modal>
  );
}
