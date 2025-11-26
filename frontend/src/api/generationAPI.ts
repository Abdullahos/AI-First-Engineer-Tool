import { SpecRequest, JobSubmissionResponse, StatusResponse, GeneratedOutput } from '../models/types';

const API_BASE_URL = 'http://localhost:8080/api'; // Corrected backend port

export const submitGenerationRequest = async (specRequest: SpecRequest): Promise<JobSubmissionResponse> => {
    const response = await fetch(`${API_BASE_URL}/generate`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(specRequest),
    });

    if (!response.ok) {
        // Attempt to parse error message from response body
        let errorMessage = `HTTP error! status: ${response.status}`;
        try {
            const errorData = await response.json();
            errorMessage = errorData.message || errorMessage;
        } catch (e) {
            // If response is not JSON, use default message
        }
        throw new Error(errorMessage);
    }

    return response.json();
};

export const fetchJobStatus = async (jobId: string): Promise<StatusResponse> => {
    const response = await fetch(`${API_BASE_URL}/status/${jobId}`);

    if (!response.ok) {
        // Attempt to parse error message from response body
        let errorMessage = `HTTP error! status: ${response.status}`;
        try {
            const errorData = await response.json();
            errorMessage = errorData.message || errorMessage;
        } catch (e) {
            // If response is not JSON, use default message
        }
        throw new Error(errorMessage);
    }

    return response.json();
};

export const fetchGeneratedOutput = async (jobId: string): Promise<GeneratedOutput> => {
    const response = await fetch(`${API_BASE_URL}/result/${jobId}`);

    if (!response.ok) {
        let errorMessage = `HTTP error! status: ${response.status}`;
        try {
            const errorData = await response.json();
            errorMessage = errorData.message || errorMessage;
        } catch (e) {
            // If response is not JSON, use default message
        }
        throw new Error(errorMessage);
    }

    return response.json();
};
