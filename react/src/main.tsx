import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {App} from "./App.tsx";
import {Provider} from "react-redux";
import {store} from "./store";
import {NotificationProvider, ThemeProvider} from "./contexts";
import {ThemeContextProvider} from "./contexts/ThemeContext.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <Provider store={store}>
          <ThemeContextProvider>
              <NotificationProvider>
                  <ThemeProvider>
                      <App />
                  </ThemeProvider>
              </NotificationProvider>
          </ThemeContextProvider>
      </Provider>
  </StrictMode>,
)


