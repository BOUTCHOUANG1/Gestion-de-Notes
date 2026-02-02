import { useState } from 'react';
import { useLocalStorage } from '../hooks/useLocalStorage';

type Language = 'en' | 'fr';

export const LanguageSwitcher = () => {
  const [language, setLanguage] = useLocalStorage<Language>('managenotes-language', 'en');
  const [isOpen, setIsOpen] = useState(false);

  const languages: { code: Language; label: string }[] = [
    { code: 'en', label: 'EN' },
    { code: 'fr', label: 'FR' },
  ];

  const handleSelect = (code: Language) => {
    setLanguage(code);
    setIsOpen(false);
  };

  return (
    <div className="lang-switcher">
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="lang-switcher-button"
        aria-label="Select language"
        aria-expanded={isOpen}
      >
        {language.toUpperCase()}
      </button>
      {isOpen && (
        <div className="lang-switcher-dropdown">
          {languages.map((lang) => (
            <button
              key={lang.code}
              onClick={() => handleSelect(lang.code)}
              className={`lang-switcher-option ${language === lang.code ? 'active' : ''}`}
            >
              {lang.label}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};
