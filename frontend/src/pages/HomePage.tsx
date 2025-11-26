import React, { useState, useEffect } from 'react';
import SpecForm from '../components/SpecForm';
import ResultsDisplay from '../components/ResultsDisplay';
import useGenerationStatus from '../hooks/useGenerationStatus';
import { submitGenerationRequest, fetchGeneratedOutput } from '../api/generationAPI';
import { SpecRequest, JobSubmissionResponse, GeneratedOutput } from '../models/types';

const HomePage: React.FC = () => {
    const [jobId, setJobId] = useState<string | null>(null);
    const { status, errorMessage, isLoading } = useGenerationStatus(jobId);
    const [generatedOutput, setGeneratedOutput] = useState<GeneratedOutput | null>(null);
    const [submissionError, setSubmissionError] = useState<string | null>(null);

    useEffect(() => {
        if (status === 'COMPLETE' && jobId) {
            fetchGeneratedOutput(jobId)
                .then(output => {
                    setGeneratedOutput(output);
                })
                .catch(error => {
                    console.error('Failed to fetch generated output:', error);
                    setSubmissionError(`Failed to load results: ${error.message}`);
                });
        } else if (status === 'FAILED') {
            setGeneratedOutput(null); // Clear previous output on failure
        }
    }, [status, jobId]);

    const handleSubmit = async (specRequest: SpecRequest) => {
        setSubmissionError(null); // Clear previous errors
        setGeneratedOutput(null); // Clear previous output
        setJobId(null); // Reset job ID to stop previous polling

        try {
            const response: JobSubmissionResponse = await submitGenerationRequest(specRequest);
            setJobId(response.jobId);
            console.log('Job submitted with ID:', response.jobId);
        } catch (error: any) {
            console.error('Error submitting request:', error);
            setSubmissionError(error.message || 'Failed to submit generation request.');
        }
    };

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-3xl font-bold text-center mb-8">AI-First Code Generation System</h1>

            {submissionError && (
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                    <strong className="font-bold">Error!</strong>
                    <span className="block sm:inline"> {submissionError}</span>
                </div>
            )}

            <SpecForm onSubmit={handleSubmit} />

            {isLoading && (
                <div className="mt-8 p-4 text-center text-blue-600 font-semibold">
                    Generating code... Current Status: {status}
                </div>
            )}

            {status === 'FAILED' && errorMessage && (
                <div className="mt-8 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
                    <strong className="font-bold">Generation Failed!</strong>
                    <span className="block sm:inline"> {errorMessage}</span>
                </div>
            )}

            {generatedOutput && status === 'COMPLETE' && (
                <ResultsDisplay generatedOutput={generatedOutput} />
            )}
        </div>
    );
};

export default HomePage;
