# ManageNotes Design System v2

## Reference
- Primary: fullstackopen.com (dark theme, monospace, illustrations)
- Components: ui-layouts.com (microinteractions)

---

## Theme System

### Dark Theme (Default)
```css
:root[data-theme="dark"] {
  --bg-primary: #33332D;
  --bg-secondary: #2A2A25;
  --bg-card: #FFFFFF;
  --text-primary: #FFFFFF;
  --text-secondary: #B8B8B0;
  --text-on-card: #33332D;
  --border-color: #FFFFFF;
  --border-subtle: #4A4A42;
  --accent: #FFFFFF;
}
```

### Light Theme
```css
:root[data-theme="light"] {
  --bg-primary: #FFFFFF;
  --bg-secondary: #F5F5F3;
  --bg-card: #FFFFFF;
  --text-primary: #33332D;
  --text-secondary: #6B6B63;
  --text-on-card: #33332D;
  --border-color: #33332D;
  --border-subtle: #E5E5E0;
  --accent: #33332D;
}
```

---

## Typography

```css
@import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Mono:wght@400;500;600;700&family=IBM+Plex+Sans:wght@400;500;600&display=swap');

--font-mono: 'IBM Plex Mono', monospace;  /* Headings, code, logo */
--font-sans: 'IBM Plex Sans', sans-serif; /* Body text */

/* Headings - Monospace, spaced */
h1, h2, h3 {
  font-family: var(--font-mono);
  letter-spacing: 0.02em;
  font-weight: 700;
}

/* Scale */
--text-xs: 0.75rem;    /* 12px */
--text-sm: 0.875rem;   /* 14px */
--text-base: 1rem;     /* 16px */
--text-lg: 1.125rem;   /* 18px */
--text-xl: 1.25rem;    /* 20px */
--text-2xl: 1.5rem;    /* 24px */
--text-3xl: 1.875rem;  /* 30px */
--text-4xl: 2.25rem;   /* 36px */
--text-5xl: 3rem;      /* 48px */
```

---

## Colors

### Status Colors
```css
--color-success: #10B981;
--color-warning: #F59E0B;
--color-error: #EF4444;
--color-info: #3B82F6;
```

### Grade Colors
```css
--grade-A: #10B981;  /* 16-20: Excellent */
--grade-B: #3B82F6;  /* 14-16: Good */
--grade-C: #F59E0B;  /* 12-14: Average */
--grade-D: #8B5CF6;  /* 10-12: Pass */
--grade-F: #EF4444;  /* <10: Fail */
```

---

## Components

### Header/Navigation
```css
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  border-bottom: 1px solid var(--border-subtle);
}

.logo {
  font-family: var(--font-mono);
  font-size: 1.25rem;
  padding: 0.5rem 1rem;
  border: 2px solid var(--border-color);
  border-radius: 4px;
}

.nav-link {
  font-family: var(--font-mono);
  font-size: 0.875rem;
  padding: 0.5rem 1rem;
  transition: opacity 0.2s;
}
.nav-link:hover {
  opacity: 0.7;
}
```

### Theme Toggle
```css
.theme-toggle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.3s ease;
}
.theme-toggle:hover {
  transform: rotate(15deg);
}
/* Sun icon for dark mode, Moon icon for light mode */
```

### Language Switcher
```css
.lang-switcher {
  font-family: var(--font-mono);
  padding: 0.5rem 1rem;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  background: transparent;
  color: var(--text-primary);
}
```

### Cards (fullstackopen style)
```css
.card {
  background: var(--bg-card);
  border: 2px solid var(--border-color);
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.card:hover {
  transform: translateY(-4px);
  box-shadow: 8px 8px 0 var(--border-color);
}

.card-image {
  aspect-ratio: 4/3;
  background: var(--bg-card);
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 2px solid var(--border-color);
}

.card-content {
  padding: 1.5rem;
  color: var(--text-on-card);
}

.card-title {
  font-family: var(--font-mono);
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}
```

### Buttons
```css
.btn-primary {
  font-family: var(--font-mono);
  padding: 0.75rem 1.5rem;
  border: 2px solid var(--border-color);
  background: transparent;
  color: var(--text-primary);
  font-weight: 500;
  transition: all 0.2s ease;
}
.btn-primary:hover {
  background: var(--text-primary);
  color: var(--bg-primary);
}

.btn-filled {
  background: var(--text-primary);
  color: var(--bg-primary);
}
.btn-filled:hover {
  background: transparent;
  color: var(--text-primary);
}
```

### Tables
```css
.table {
  width: 100%;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  overflow: hidden;
}

.table th {
  font-family: var(--font-mono);
  text-align: left;
  padding: 1rem;
  background: var(--bg-secondary);
  border-bottom: 2px solid var(--border-color);
  font-weight: 600;
}

.table td {
  padding: 1rem;
  border-bottom: 1px solid var(--border-subtle);
}

.table tr:hover {
  background: var(--bg-secondary);
}
```

### Grade Badge
```css
.grade-badge {
  font-family: var(--font-mono);
  font-weight: 700;
  padding: 0.25rem 0.75rem;
  border-radius: 4px;
  font-size: 0.875rem;
}
.grade-A { background: var(--grade-A); color: white; }
.grade-B { background: var(--grade-B); color: white; }
.grade-C { background: var(--grade-C); color: white; }
.grade-D { background: var(--grade-D); color: white; }
.grade-F { background: var(--grade-F); color: white; }
```

### Forms
```css
.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  font-family: var(--font-mono);
  font-size: 0.875rem;
  font-weight: 500;
  margin-bottom: 0.5rem;
  display: block;
}

.form-input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid var(--border-color);
  border-radius: 4px;
  background: transparent;
  color: var(--text-primary);
  font-size: 1rem;
  transition: box-shadow 0.2s ease;
}
.form-input:focus {
  outline: none;
  box-shadow: 4px 4px 0 var(--border-color);
}
```

---

## Illustrations

Use hand-drawn style SVG illustrations for:
- Empty states
- Section headers
- Loading states
- Error pages

Style: Black & white line art, playful, educational

Sources:
- undraw.co (customize to black/white)
- Custom SVG illustrations matching fullstackopen style

---

## Microinteractions

### Hover Effects
```css
/* Card lift */
.card:hover {
  transform: translateY(-4px);
  box-shadow: 8px 8px 0 var(--border-color);
}

/* Button fill */
.btn:hover {
  background: var(--text-primary);
  color: var(--bg-primary);
}

/* Link underline */
.link:hover {
  text-decoration: underline;
  text-underline-offset: 4px;
}
```

### Transitions
```css
--transition-fast: 150ms ease;
--transition-normal: 200ms ease;
--transition-slow: 300ms ease;

/* Apply to interactive elements */
* {
  transition: background var(--transition-fast),
              color var(--transition-fast),
              border-color var(--transition-fast),
              transform var(--transition-normal),
              box-shadow var(--transition-normal);
}
```

### Page Transitions
```css
/* Fade in on route change */
.page-enter {
  opacity: 0;
  transform: translateY(10px);
}
.page-enter-active {
  opacity: 1;
  transform: translateY(0);
  transition: all 0.3s ease;
}
```

### Loading Skeleton
```css
.skeleton {
  background: linear-gradient(
    90deg,
    var(--bg-secondary) 25%,
    var(--border-subtle) 50%,
    var(--bg-secondary) 75%
  );
  background-size: 200% 100%;
  animation: skeleton-pulse 1.5s ease-in-out infinite;
  border-radius: 4px;
}

@keyframes skeleton-pulse {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}
```

---

## Responsive Breakpoints

```css
--breakpoint-sm: 640px;
--breakpoint-md: 768px;
--breakpoint-lg: 1024px;
--breakpoint-xl: 1280px;

/* Mobile first */
@media (min-width: 640px) { /* Tablet */ }
@media (min-width: 1024px) { /* Desktop */ }
```

---

## Layout Templates

### Login Page
```
┌─────────────────────────────────────────────────┐
│  [Logo]                    [☀️] [EN ▼]          │
├─────────────────────────────────────────────────┤
│                                                 │
│     ┌─────────────────────────────────┐         │
│     │  [Illustration]                 │         │
│     │                                 │         │
│     │  Welcome to ManageNotes         │         │
│     │  University Grade Management    │         │
│     │                                 │         │
│     │  ┌─────────────────────────┐    │         │
│     │  │ Username                │    │         │
│     │  └─────────────────────────┘    │         │
│     │  ┌─────────────────────────┐    │         │
│     │  │ Password                │    │         │
│     │  └─────────────────────────┘    │         │
│     │                                 │         │
│     │  [ Sign In ]                    │         │
│     │                                 │         │
│     └─────────────────────────────────┘         │
│                                                 │
└─────────────────────────────────────────────────┘
```

### Dashboard
```
┌──────────────────────────────────────────────────┐
│  [Logo]  Dashboard  Grades  Profile  [☀️] [EN ▼] │
├──────────────────────────────────────────────────┤
│                                                  │
│  Welcome back, John!                             │
│                                                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐       │
│  │ [illust] │  │ [illust] │  │ [illust] │       │
│  │ Courses  │  │ Grades   │  │ GPA      │       │
│  │ 6        │  │ 24       │  │ 15.5     │       │
│  └──────────┘  └──────────┘  └──────────┘       │
│                                                  │
│  Recent Grades                                   │
│  ┌──────────────────────────────────────────┐   │
│  │ Subject      │ Grade │ Date              │   │
│  ├──────────────────────────────────────────┤   │
│  │ Mathematics  │ [A]   │ Jan 15, 2026      │   │
│  │ Physics      │ [B]   │ Jan 14, 2026      │   │
│  └──────────────────────────────────────────┘   │
│                                                  │
└──────────────────────────────────────────────────┘
```

---

## Implementation Checklist

### Phase 1: Foundation
- [ ] Add CSS variables to index.css
- [ ] Import IBM Plex fonts
- [ ] Implement theme toggle (localStorage)
- [ ] Implement language switcher

### Phase 2: Components
- [ ] Redesign Header with logo, nav, theme toggle, lang switcher
- [ ] Create Card component (fullstackopen style)
- [ ] Create Button variants
- [ ] Create Form components
- [ ] Create Table component with grade badges

### Phase 3: Pages
- [ ] Redesign Login page
- [ ] Redesign Dashboard
- [ ] Redesign Grade tables
- [ ] Add illustrations

### Phase 4: Polish
- [ ] Add microinteractions
- [ ] Add page transitions
- [ ] Add skeleton loaders
- [ ] Test responsive design
- [ ] Test dark/light themes
