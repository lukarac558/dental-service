import { Lookup } from './lookup.model';

export type Visit = {
    id: number;
    startDate: Date;
    endDate: Date;
    doctorName: string;
    doctorGender: string;
    doctorSpecialization: string;
    services: Lookup[]
};

export type VisitForm = {
    doctorId: number;
    date: Date | null;
    startHour: string;
    serviceIds: number[] | null;
};

export type VisitAvailableDate = {
    date: Date;
    hours: string[];
};

export type ReservationVisit = Visit & {
    reservationEndDate: Date;
};
