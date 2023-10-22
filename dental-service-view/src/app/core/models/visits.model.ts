import { Lookup } from './lookup.model';

export type Visit = {
    id: number;
    startDate: Date;
    endDate: Date;
    doctorName: string;
    doctorGender: number;
    doctorSpecialization: string;
    services: Lookup[]
};
