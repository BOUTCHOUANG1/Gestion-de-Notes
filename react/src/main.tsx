import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {App} from "./App.tsx";
import {Provider} from "react-redux";
import {store} from "./store";
import {NotificationProvider} from "./contexts";

// Clean up legacy theme preference
localStorage.removeItem('managenotes-theme');

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <Provider store={store}>
          <NotificationProvider>
              <App />
          </NotificationProvider>
      </Provider>
  </StrictMode>,
)

