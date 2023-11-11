import { Moment } from 'moment';

export type DoctorShort = {
    id: number;
    name: string;
    surname: string;
    sex: string;
    competencyInformation: DoctorCompetency
};

export type DoctorSearch = {
    name: string;
    service: string;
};

export type Doctor = DoctorShort & {
    serviceTypes: DoctorService[]
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
    id: number;
    startDate: string;
    workDuration: string;
    doctorId: number;
};

export type DoctorAddScheduleForm = {
    startDate: Moment | null;
    startTime: string;
    workDuration: string;
};

export type DoctorAddSchedule = {
    startDate: string;
    workDuration: string;
    doctorId: number;
};
