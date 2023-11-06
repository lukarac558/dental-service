import { Lookup } from './lookup.model';

export type DoctorShort = {
    id: number;
    firstName: string;
    lastName: string;
    gender: string;
    specialization: string;
    aboutMe: string;
};

export type DoctorSearch = {
    name: string;
    serviceName: string;
};

export type Doctor = DoctorShort & {
    services: Lookup[]
};

export type DoctorCompetency = {
    id: number | null;
    title: string;
    description: string;
};

export type DoctorCompetencyForm = DoctorCompetency;

export type DoctorService = {
    id: number;
    name: string;
    durationTime: string;
    description: string;
};

export type DoctorServiceForm = Pick<DoctorService, 'name' | 'durationTime' | 'description'>;

export type DoctorSchedule = {
    startDate: Date | null;
    workDuration: string;
};

export type DoctorAddScheduleForm = DoctorSchedule & {
    startTime: string;
};
