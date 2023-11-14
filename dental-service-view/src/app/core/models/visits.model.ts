import { Moment } from 'moment';

import { DoctorServiceAndDoctorInfo } from './doctor.model';

export type Visit = {
    visit: VisitDetails;
    visitPositionDetails: VisitPositiondetails[];
};

export type VisitDetails = {
    id: number;
    startDate: string;
    endDate: string;
    reservationDate: string;
    description: string;
};

export type VisitPositiondetails = {
    id: number;
    serviceType: DoctorServiceAndDoctorInfo
};

export type VisitForm = {
    date: Date | null;
    startHour: string;
    serviceIds: number[] | null;
};

export type VisitAvailableDate = {
    date: Moment;
    hours: string[];
};
