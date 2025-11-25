import React, { useState } from 'react';
import { GeneratedOutput } from '../models/types';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { materialDark } from 'react-syntax-highlighter/dist/esm/styles/prism';

interface ResultsDisplayProps {
    generatedOutput: GeneratedOutput | null;
}

const ResultsDisplay: React.FC<ResultsDisplayProps> = ({ generatedOutput }) => {
    const [activeTab, setActiveTab] = useState<'backend' | 'frontend' | 'test' | 'folder'>('backend');

    if (!generatedOutput) {
        return (
            <div className="p-4 text-center text-gray-500">
                No generated output to display yet.
            </div>
        );
    }

    const renderContent = () => {
        switch (activeTab) {
            case 'backend':
                return (
                    <SyntaxHighlighter language="java" style={materialDark}>
                        {generatedOutput.backendCode}
                    </SyntaxHighlighter>
                );
            case 'frontend':
                return (
                    <SyntaxHighlighter language="typescript" style={materialDark}>
                        {generatedOutput.frontendCode}
                    </SyntaxHighlighter>
                );
            case 'test':
                return (
                    <SyntaxHighlighter language="java" style={materialDark}>
                        {generatedOutput.testCode}
                    </SyntaxHighlighter>
                );
            case 'folder':
                return (
                    <SyntaxHighlighter language="bash" style={materialDark}>
                        {generatedOutput.folderStructure}
                    </SyntaxHighlighter>
                );
            default:
                return null;
        }
    };

    const tabButtonClass = (tabName: string) =>
        `py-2 px-4 text-sm font-medium ${
            activeTab === tabName
                ? 'border-b-2 border-indigo-500 text-indigo-600'
                : 'text-gray-500 hover:text-gray-700'
        }`;

    return (
        <div className="mt-8 bg-white shadow-md rounded-lg">
            <div className="border-b border-gray-200">
                <nav className="-mb-px flex space-x-8 px-4" aria-label="Tabs">
                    <button
                        onClick={() => setActiveTab('backend')}
                        className={tabButtonClass('backend')}
                    >
                        Backend Code
                    </button>
                    <button
                        onClick={() => setActiveTab('frontend')}
                        className={tabButtonClass('frontend')}
                    >
                        Frontend Code
                    </button>
                    <button
                        onClick={() => setActiveTab('test')}
                        className={tabButtonClass('test')}
                    >
                        Test Code
                    </button>
                    <button
                        onClick={() => setActiveTab('folder')}
                        className={tabButtonClass('folder')}
                    >
                        Folder Structure
                    </button>
                </nav>
            </div>
            <div className="p-4 overflow-auto max-h-96">
                {renderContent()}
            </div>
        </div>
    );
};

export default ResultsDisplay;
