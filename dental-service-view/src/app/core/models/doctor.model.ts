import { Lookup } from './lookup.model';

export type DoctorShort = {
    id: number;
    firstName: string;
    lastName: string;
    gender: number;
    specialization: string;
    aboutMe: string;
    services: Lookup[]
};

export type DoctorSearch = {
    name: string;
    serviceId: number | null;
};

export type Doctor = DoctorShort & {};
