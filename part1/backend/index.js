const express = require('express');
const bodyParser = require('body-parser');
const Chatkit = require('@pusher/chatkit-server');
const app = express();

const chatkit = new Chatkit.default({
  instanceLocator: 'v1:us1:3bff3c2b-c602-4626-9da9-66e758fb238a',
  key:
    '99a5f6d5-cd3d-4c90-91ee-457d74799e06:2lEQ7khknbSiiugeD3WRSmNq/hKXNFecvkTkeDWqFH0='
});
//app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.post('/token', (req, res) => {
  const result = chatkit.authenticate({
    userId: req.query.user_id
  });
  res.status(result.status).send(result.body);
});

const server = app.listen(3000, () => {
  console.log(`Express server running on port ${server.address().port}`);
});
