const express = require("express");
const app = express();

app.use(express.json());

app.get("/", (req, res) => {
    res.send("Backend working!");
});

app.listen(5000, () => {
    console.log("Server running on port 5000");
});
node server.js
app.listen(5000)
