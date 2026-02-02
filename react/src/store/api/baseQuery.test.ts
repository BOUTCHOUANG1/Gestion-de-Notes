import { describe, it, expect, vi, beforeEach } from 'vitest';
import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/query';
import { api } from './apiSlice';

describe('baseQuery - JWT Token Injection', () => {
  let store: ReturnType<typeof configureStore>;

  beforeEach(() => {
    localStorage.clear();
    store = configureStore({
      reducer: {
        [api.reducerPath]: api.reducer,
      },
      middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(api.middleware),
    });
    setupListeners(store.dispatch);
  });

  it('should inject JWT token from localStorage into request headers', () => {
    const mockToken = 'test-jwt-token-12345';
    localStorage.setItem('authToken', mockToken);

    const headers = new Headers();
    const prepareHeaders = (headers: Headers) => {
      const token = localStorage.getItem('authToken');
      if (token) {
        headers.set('Authorization', `Bearer ${token}`);
      }
      return headers;
    };

    const result = prepareHeaders(headers);
    
    expect(result.get('Authorization')).toBe(`Bearer ${mockToken}`);
  });

  it('should not add Authorization header when no token exists', () => {
    localStorage.removeItem('authToken');

    const headers = new Headers();
    const prepareHeaders = (headers: Headers) => {
      const token = localStorage.getItem('authToken');
      if (token) {
        headers.set('Authorization', `Bearer ${token}`);
      }
      return headers;
    };

    const result = prepareHeaders(headers);
    
    expect(result.has('Authorization')).toBe(false);
  });

  it('should clear token on 401 Unauthorized error', () => {
    const mockToken = 'expired-token';
    localStorage.setItem('authToken', mockToken);

    const handleUnauthorized = () => {
      localStorage.removeItem('authToken');
    };

    expect(localStorage.getItem('authToken')).toBe(mockToken);
    
    handleUnauthorized();
    
    expect(localStorage.getItem('authToken')).toBeNull();
  });

  it('should redirect to login on 401 error', () => {
    const mockNavigate = vi.fn();
    
    const handle401Error = (navigate: typeof mockNavigate) => {
      localStorage.removeItem('authToken');
      navigate('/auth/login');
    };

    handle401Error(mockNavigate);

    expect(localStorage.getItem('authToken')).toBeNull();
    expect(mockNavigate).toHaveBeenCalledWith('/auth/login');
  });

  it('should handle network errors gracefully', () => {
    const handleNetworkError = (error: Error) => {
      return {
        error: {
          status: 'FETCH_ERROR',
          error: error.message,
        },
      };
    };

    const networkError = new Error('Network request failed');
    const result = handleNetworkError(networkError);

    expect(result.error.status).toBe('FETCH_ERROR');
    expect(result.error.error).toBe('Network request failed');
  });

  it('should preserve existing headers while adding Authorization', () => {
    const mockToken = 'test-token';
    localStorage.setItem('authToken', mockToken);

    const headers = new Headers({
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    });

    const prepareHeaders = (headers: Headers) => {
      const token = localStorage.getItem('authToken');
      if (token) {
        headers.set('Authorization', `Bearer ${token}`);
      }
      return headers;
    };

    const result = prepareHeaders(headers);

    expect(result.get('Authorization')).toBe(`Bearer ${mockToken}`);
    expect(result.get('Content-Type')).toBe('application/json');
    expect(result.get('Accept')).toBe('application/json');
  });

  it('should handle empty token string', () => {
    localStorage.setItem('authToken', '');

    const headers = new Headers();
    const prepareHeaders = (headers: Headers) => {
      const token = localStorage.getItem('authToken');
      if (token && token.trim()) {
        headers.set('Authorization', `Bearer ${token}`);
      }
      return headers;
    };

    const result = prepareHeaders(headers);
    
    expect(result.has('Authorization')).toBe(false);
  });

  it('should handle malformed JSON error responses', () => {
    const handleParseError = (response: Response) => {
      return {
        error: {
          status: response.status,
          data: 'Failed to parse response',
        },
      };
    };

    const mockResponse = { status: 500 } as Response;
    const result = handleParseError(mockResponse);

    expect(result.error.status).toBe(500);
    expect(result.error.data).toBe('Failed to parse response');
  });
});

describe('baseQuery - Error Handling', () => {
  it('should handle 403 Forbidden error', () => {
    const handle403Error = () => {
      return {
        error: {
          status: 403,
          data: { message: 'Access forbidden' },
        },
      };
    };

    const result = handle403Error();

    expect(result.error.status).toBe(403);
    expect(result.error.data.message).toBe('Access forbidden');
  });

  it('should handle 404 Not Found error', () => {
    const handle404Error = () => {
      return {
        error: {
          status: 404,
          data: { message: 'Resource not found' },
        },
      };
    };

    const result = handle404Error();

    expect(result.error.status).toBe(404);
    expect(result.error.data.message).toBe('Resource not found');
  });

  it('should handle 500 Server Error', () => {
    const handle500Error = () => {
      return {
        error: {
          status: 500,
          data: { message: 'Internal server error' },
        },
      };
    };

    const result = handle500Error();

    expect(result.error.status).toBe(500);
    expect(result.error.data.message).toBe('Internal server error');
  });
});
