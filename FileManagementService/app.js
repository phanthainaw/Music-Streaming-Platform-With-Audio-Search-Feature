const express = require('express');
const multer = require('multer');
const path = require('path');
const cors = require('cors');

const app = express();
const PORT = 3000;

app.use(cors());

const tracksUploadFolder = path.join(__dirname, 'uploads', 'Track');
const coversUploadFolder = path.join(__dirname, 'uploads', 'Cover');
const avatarsUploadFolder = path.join(__dirname, 'uploads', 'Avatar');

// Storage configs
const trackStorage = multer.diskStorage({
  destination: (_, __, cb) => cb(null, tracksUploadFolder),
  filename: (_, file, cb) => {
    const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1e9);
    cb(null, file.fieldname + '-' + uniqueSuffix + path.extname(file.originalname));
  }
});
const coverStorage = multer.diskStorage({
  destination: (_, __, cb) => cb(null, coversUploadFolder),
  filename: (_, file, cb) => {
    const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1e9);
    cb(null, file.fieldname + '-' + uniqueSuffix + path.extname(file.originalname));
  }
});
const avatarStorage = multer.diskStorage({
  destination: (_, __, cb) => cb(null, avatarsUploadFolder),
  filename: (_, file, cb) => {
    const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1e9);
    cb(null, file.fieldname + '-' + uniqueSuffix + path.extname(file.originalname));
  }
});

const trackUpload = multer({ storage: trackStorage });
const coverUpload = multer({ storage: coverStorage });
const avatarUpload = multer({ storage: avatarStorage });

// Serve static files
app.use('/uploads/tracks', express.static(tracksUploadFolder));
app.use('/uploads/covers', express.static(coversUploadFolder));
app.use('/uploads/avatars', express.static(avatarsUploadFolder));

// Routes
app.post('/upload/track', trackUpload.single('file'), (req, res) => {
  if (!req.file) return res.status(400).json({ error: 'No file uploaded' });
  res.json({ url: `http://localhost:${PORT}/uploads/tracks/${req.file.filename}` });
});
app.post('/upload/cover', coverUpload.single('file'), (req, res) => {
  if (!req.file) return res.status(400).json({ error: 'No file uploaded' });
  res.json({ url: `http://localhost:${PORT}/uploads/covers/${req.file.filename}` });
});
app.post('/upload/avatar', avatarUpload.single('file'), (req, res) => {
  if (!req.file) return res.status(400).json({ error: 'No file uploaded' });
  res.json({ url: `http://localhost:${PORT}/uploads/avatars/${req.file.filename}` });
});

app.listen(PORT, () => {
  console.log(`File server running on http://localhost:${PORT}`);
});
