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
    startDate: Date | null;
    workDuration: string;
};

export type DoctorAddScheduleForm = DoctorSchedule & {
    startTime: string;
};
