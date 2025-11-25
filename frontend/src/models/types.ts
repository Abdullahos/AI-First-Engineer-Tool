export interface SpecRequest {
    systemDescription: string;
    functionalRequirements: string[];
    nonFunctionalRequirements: string[];
    dataModel: string;
    apiDesign: string;
    acceptanceCriteria: string[];
    isRealModel: boolean;
}

export interface JobSubmissionResponse {
    jobId: string;
}

export interface StatusResponse {
    status: string;
    errorMessage?: string;
}

export interface GeneratedOutput {
    id: string;
    backendCode: string;
    frontendCode: string;
    testCode: string;
    folderStructure: string;
}
