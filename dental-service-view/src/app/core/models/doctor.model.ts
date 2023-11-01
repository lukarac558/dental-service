import { Lookup } from './lookup.model';

export type DoctorShort = {
    id: number;
    firstName: string;
    lastName: string;
    gender: number;
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

export type DoctorInfo = Pick<DoctorShort, 'specialization' | 'aboutMe'> & {
    services: Lookup[]
};

export type DoctorInfoForm = Pick<DoctorShort, 'specialization' | 'aboutMe'>;

export type DoctorSchedule = {
    startDate: Date | null;
    workDuration: string;
};

export type DoctorAddScheduleForm = DoctorSchedule & {
    startTime: string;
};
