import { Moment } from 'moment';

import { DoctorService, DoctorShort } from './doctor.model';

export type Visit = {
    visit: VisitDetails;
    visitPositionDetails: VisitPositiondetails[];
    doctorInfo: DoctorShort;
};

export type VisitDetails = {
    id: number;
    startDate: string;
    reservationDate: string;
    description: string;
};

export type VisitPositiondetails = {
    id: number;
    serviceType: DoctorService
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
