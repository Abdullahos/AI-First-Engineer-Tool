import { useState, useEffect, useRef } from 'react';
import { StatusResponse } from '../models/types';
import { fetchJobStatus } from '../api/generationAPI';

const POLLING_INTERVAL_MS = 5000; // 5 seconds

const useGenerationStatus = (jobId: string | null) => {
    const [status, setStatus] = useState<string>('IDLE');
    const [errorMessage, setErrorMessage] = useState<string | undefined>(undefined);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const intervalRef = useRef<number | undefined>(undefined);

    const stopPolling = () => {
        if (intervalRef.current) {
            clearInterval(intervalRef.current);
            intervalRef.current = undefined;
        }
    };

    useEffect(() => {
        if (!jobId) {
            stopPolling();
            setStatus('IDLE');
            setErrorMessage(undefined);
            setIsLoading(false);
            return;
        }

        setIsLoading(true);
        setStatus('PENDING'); // Assume pending when a jobId is set

        const pollStatus = async () => {
            try {
                const response = await fetchJobStatus(jobId);
                setStatus(response.status);
                setErrorMessage(response.errorMessage);

                if (response.status === 'COMPLETE' || response.status === 'FAILED') {
                    stopPolling();
                    setIsLoading(false);
                } else {
                    setIsLoading(true);
                }
            } catch (error: any) {
                console.error('Polling error:', error);
                stopPolling();
                setStatus('FAILED');
                setErrorMessage(error.message || 'Unknown error during polling');
                setIsLoading(false);
            }
        };

        // Initial poll immediately
        pollStatus();

        // Start polling
        stopPolling(); // Clear any existing interval before starting a new one
        intervalRef.current = window.setInterval(pollStatus, POLLING_INTERVAL_MS);

        // Cleanup function
        return () => {
            stopPolling();
        };
    }, [jobId]); // Re-run effect only when jobId changes

    return { status, errorMessage, isLoading };
};

export default useGenerationStatus;
