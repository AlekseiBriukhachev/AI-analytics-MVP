# AI Analytics UI

React dashboard for uploading datasets, training models, and requesting predictions from the AI Analytics API.

## Run locally

1. Install Node.js and npm.
2. Start the backend API on `http://localhost:8080`.
3. In this directory, install dependencies and start the UI:

   ```bash
   npm install
   npm start
   ```

   The UI opens at `http://localhost:3000`. API requests are proxied to the local backend.

## Validate

```bash
npm test -- --watchAll=false
npm run build
```
