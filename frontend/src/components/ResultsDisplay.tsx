import React, { useState } from 'react';
import { GeneratedOutput } from '../models/types';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { materialDark } from 'react-syntax-highlighter/dist/esm/styles/prism';

interface ResultsDisplayProps {
    generatedOutput: GeneratedOutput | null;
}

const ResultsDisplay: React.FC<ResultsDisplayProps> = ({ generatedOutput }) => {
    const [activeTab, setActiveTab] = useState<'backend' | 'frontend' | 'test' | 'folder'>('backend');
    const [copyButtonText, setCopyButtonText] = useState('Copy');

    if (!generatedOutput) {
        return (
            <div className="p-4 text-center text-gray-500">
                No generated output to display yet.
            </div>
        );
    }

    const handleCopy = () => {
        let codeToCopy = '';
        switch (activeTab) {
            case 'backend':
                codeToCopy = generatedOutput.backendCode;
                break;
            case 'frontend':
                codeToCopy = generatedOutput.frontendCode;
                break;
            case 'test':
                codeToCopy = generatedOutput.testCode;
                break;
            default:
                return;
        }

        navigator.clipboard.writeText(codeToCopy).then(() => {
            setCopyButtonText('Copied!');
            setTimeout(() => setCopyButtonText('Copy'), 2000);
        }, (err) => {
            console.error('Could not copy text: ', err);
            setCopyButtonText('Failed!');
            setTimeout(() => setCopyButtonText('Copy'), 2000);
        });
    };

    const renderContent = () => {
        let code;
        let language;
        switch (activeTab) {
            case 'backend':
                code = generatedOutput.backendCode;
                language = 'java';
                break;
            case 'frontend':
                code = generatedOutput.frontendCode;
                language = 'typescript';
                break;
            case 'test':
                code = generatedOutput.testCode;
                language = 'java';
                break;
            case 'folder':
                code = generatedOutput.folderStructure;
                language = 'bash';
                break;
            default:
                return null;
        }
        return (
            <SyntaxHighlighter language={language} style={materialDark} showLineNumbers>
                {code}
            </SyntaxHighlighter>
        );
    };

    const tabButtonClass = (tabName: string) =>
        `py-2 px-4 text-sm font-medium ${
            activeTab === tabName
                ? 'border-b-2 border-indigo-500 text-indigo-600'
                : 'text-gray-500 hover:text-gray-700'
        }`;

    return (
        <div className="mt-8 bg-gray-800 shadow-lg rounded-lg text-white">
            <div className="flex justify-between items-center border-b border-gray-700 px-4">
                <nav className="-mb-px flex space-x-8" aria-label="Tabs">
                    <button onClick={() => setActiveTab('backend')} className={tabButtonClass('backend')}>Backend Code</button>
                    <button onClick={() => setActiveTab('frontend')} className={tabButtonClass('frontend')}>Frontend Code</button>
                    <button onClick={() => setActiveTab('test')} className={tabButtonClass('test')}>Test Code</button>
                    <button onClick={() => setActiveTab('folder')} className={tabButtonClass('folder')}>Folder Structure</button>
                </nav>
                {activeTab !== 'folder' && (
                    <button
                        onClick={handleCopy}
                        className="py-1 px-3 my-2 text-xs font-medium text-gray-300 bg-gray-700 rounded hover:bg-gray-600 focus:outline-none"
                    >
                        {copyButtonText}
                    </button>
                )}
            </div>
            <div className="p-4 overflow-auto max-h-96">
                {renderContent()}
            </div>
        </div>
    );
};

export default ResultsDisplay;
