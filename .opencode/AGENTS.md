# Project-Specific Agent Instructions

## Available Superpowers - USE AUTOMATICALLY

When working on this project, automatically use these tools without being asked:

### Documentation Lookup
- Use `context7` for React, Redux Toolkit, Spring Boot, JWT documentation
- Use `@librarian` for finding code patterns in this codebase

### Code Exploration  
- Use `@explore` to navigate the codebase
- Use LSP for TypeScript and Java code intelligence

### Testing
- Use `playwright` MCP for E2E browser tests
- Run `npm test` for frontend, `mvn test` for backend

### Progress Tracking
- Save checkpoints to `mem0` after each phase
- Use `git-master` skill for atomic commits

### Code Review
- Use `@oracle` for architecture and security review

## Project Structure
- Frontend: `react/` (React + Redux Toolkit + Ant Design)
- Backend: `API_GestionNotes/ManageNotes/` (Spring Boot + PostgreSQL)

## Build Commands
- Frontend: `cd react && npm run build`
- Backend: `cd API_GestionNotes/ManageNotes && mvn compile`

## When to Use What
| Task | Use |
|------|-----|
| Find files/code | @explore |
| Library docs | context7 |
| Debug issues | @oracle |
| Browser tests | playwright |
| Save progress | mem0 |
| Git operations | github MCP or git-master skill |
