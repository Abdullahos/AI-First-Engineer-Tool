import React, { useState } from 'react';
import { SpecRequest } from '../models/types';

interface SpecFormProps {
    onSubmit: (specRequest: SpecRequest) => void;
}

const SpecForm: React.FC<SpecFormProps> = ({ onSubmit }) => {
    const [systemDescription, setSystemDescription] = useState<string>('');
    const [functionalRequirements, setFunctionalRequirements] = useState<string>('');
    const [nonFunctionalRequirements, setNonFunctionalRequirements] = useState<string>('');
    const [dataModel, setDataModel] = useState<string>('');
    const [apiDesign, setApiDesign] = useState<string>('');
    const [acceptanceCriteria, setAcceptanceCriteria] = useState<string>('');
    const [isRealModel, setIsRealModel] = useState<boolean>(false);

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();

        const formatListInput = (input: string): string[] =>
            input.split('\\n').map(s => s.trim()).filter(s => s.length > 0);

        const specRequest: SpecRequest = {
            systemDescription,
            functionalRequirements: formatListInput(functionalRequirements),
            nonFunctionalRequirements: formatListInput(nonFunctionalRequirements),
            dataModel,
            apiDesign,
            acceptanceCriteria: formatListInput(acceptanceCriteria),
            isRealModel,
        };

        onSubmit(specRequest);
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4 p-4 bg-white shadow-md rounded-lg">
            <div>
                <label htmlFor="systemDescription" className="block text-sm font-medium text-gray-700">System Description:</label>
                <textarea
                    id="systemDescription"
                    value={systemDescription}
                    onChange={(e) => setSystemDescription(e.target.value)}
                    rows={4}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    required
                ></textarea>
            </div>

            <div>
                <label htmlFor="functionalRequirements" className="block text-sm font-medium text-gray-700">Functional Requirements (one per line):</label>
                <textarea
                    id="functionalRequirements"
                    value={functionalRequirements}
                    onChange={(e) => setFunctionalRequirements(e.target.value)}
                    rows={6}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    required
                ></textarea>
            </div>

            <div>
                <label htmlFor="nonFunctionalRequirements" className="block text-sm font-medium text-gray-700">Non-Functional Requirements (one per line):</label>
                <textarea
                    id="nonFunctionalRequirements"
                    value={nonFunctionalRequirements}
                    onChange={(e) => setNonFunctionalRequirements(e.target.value)}
                    rows={6}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    required
                ></textarea>
            </div>

            <div>
                <label htmlFor="dataModel" className="block text-sm font-medium text-gray-700">Data Model:</label>
                <textarea
                    id="dataModel"
                    value={dataModel}
                    onChange={(e) => setDataModel(e.target.value)}
                    rows={8}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    required
                ></textarea>
            </div>

            <div>
                <label htmlFor="apiDesign" className="block text-sm font-medium text-gray-700">API Design:</label>
                <textarea
                    id="apiDesign"
                    value={apiDesign}
                    onChange={(e) => setApiDesign(e.target.value)}
                    rows={8}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    required
                ></textarea>
            </div>

            <div>
                <label htmlFor="acceptanceCriteria" className="block text-sm font-medium text-gray-700">Acceptance Criteria (one per line):</label>
                <textarea
                    id="acceptanceCriteria"
                    value={acceptanceCriteria}
                    onChange={(e) => setAcceptanceCriteria(e.target.value)}
                    rows={6}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    required
                ></textarea>
            </div>

            <div className="flex items-center">
                <input
                    id="isRealModel"
                    type="checkbox"
                    checked={isRealModel}
                    onChange={(e) => setIsRealModel(e.target.checked)}
                    className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                />
                <label htmlFor="isRealModel" className="ml-2 block text-sm text-gray-900">Use Real AI Model</label>
            </div>

            <button
                type="submit"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
                Generate Code
            </button>
        </form>
    );
};

export default SpecForm;
