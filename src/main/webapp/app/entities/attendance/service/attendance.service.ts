import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttendance, NewAttendance } from '../attendance.model';

export type PartialUpdateAttendance = Partial<IAttendance> & Pick<IAttendance, 'id'>;

export type EntityResponseType = HttpResponse<IAttendance>;
export type EntityArrayResponseType = HttpResponse<IAttendance[]>;

@Injectable({ providedIn: 'root' })
export class AttendanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(attendance: NewAttendance): Observable<EntityResponseType> {
    return this.http.post<IAttendance>(this.resourceUrl, attendance, { observe: 'response' });
  }

  update(attendance: IAttendance): Observable<EntityResponseType> {
    return this.http.put<IAttendance>(`${this.resourceUrl}/${this.getAttendanceIdentifier(attendance)}`, attendance, {
      observe: 'response',
    });
  }

  partialUpdate(attendance: PartialUpdateAttendance): Observable<EntityResponseType> {
    return this.http.patch<IAttendance>(`${this.resourceUrl}/${this.getAttendanceIdentifier(attendance)}`, attendance, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAttendance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttendance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAttendanceIdentifier(attendance: Pick<IAttendance, 'id'>): number {
    return attendance.id;
  }

  compareAttendance(o1: Pick<IAttendance, 'id'> | null, o2: Pick<IAttendance, 'id'> | null): boolean {
    return o1 && o2 ? this.getAttendanceIdentifier(o1) === this.getAttendanceIdentifier(o2) : o1 === o2;
  }

  addAttendanceToCollectionIfMissing<Type extends Pick<IAttendance, 'id'>>(
    attendanceCollection: Type[],
    ...attendancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const attendances: Type[] = attendancesToCheck.filter(isPresent);
    if (attendances.length > 0) {
      const attendanceCollectionIdentifiers = attendanceCollection.map(attendanceItem => this.getAttendanceIdentifier(attendanceItem)!);
      const attendancesToAdd = attendances.filter(attendanceItem => {
        const attendanceIdentifier = this.getAttendanceIdentifier(attendanceItem);
        if (attendanceCollectionIdentifiers.includes(attendanceIdentifier)) {
          return false;
        }
        attendanceCollectionIdentifiers.push(attendanceIdentifier);
        return true;
      });
      return [...attendancesToAdd, ...attendanceCollection];
    }
    return attendanceCollection;
  }
  getUserFriendlyAttendanceStatus(status: any): string {
    const object: { [index: string]: string } = {
      BLANK: 'Blank',
      BLANK_INEFFECTIVE: 'Blank Ineffective',
      WEEKLY_OFFDAY: 'Weekend',
      NON_FULFILLED_OFFICE_HOURS: 'Non Fulfilled Office Hours',
      GOVT_HOLIDAY: 'Govt. Holiday',
      MOVEMENT: 'Movement',
      PRESENT_GOVT_HOLIDAY: 'Present Govt. Holiday',
      PRESENT_WEEKLY_OFFDAY: 'Present Weekly Off Day',
      ABSENT: 'Absent',
      PRESENT: 'Present',
      LATE: 'Late',
      MENTIONABLE_ANNUAL_LEAVE: 'Annual Leave',
      MENTIONABLE_CASUAL_LEAVE: 'Casual Leave',
      NON_MENTIONABLE_COMPENSATORY_LEAVE: 'Compensatory Leave',
      NON_MENTIONABLE_PANDEMIC_LEAVE: 'Pandemic Leave',
      NON_MENTIONABLE_PATERNITY_LEAVE: 'Paternity Leave',
      NON_MENTIONABLE_MATERNITY_LEAVE: 'Maternity Leave',
      LEAVE_WITHOUT_PAY: 'Leave without Pay',
      LEAVE_WITHOUT_PAY_SANDWICH: 'Leave without Pay',
      GENERAL_HOLIDAY: 'General Holiday',
    };
    return object[status] ?? status;
  }

  getUIFriendlyAttendanceStatusDashboard(status: any): string {
    const object: { [index: string]: string } = {
      BLANK: '',
      BLANK_INEFFECTIVE: '',
      WEEKLY_OFFDAY: 'Weekend',
      NON_FULFILLED_OFFICE_HOURS: '',
      GOVT_HOLIDAY: 'Holiday',
      MOVEMENT: 'Movement',
      PRESENT_GOVT_HOLIDAY: '',
      PRESENT_WEEKLY_OFFDAY: '',
      ABSENT: 'Absent',
      PRESENT: '',
      LATE: '',
      MENTIONABLE_ANNUAL_LEAVE: 'Annual Leave',
      MENTIONABLE_CASUAL_LEAVE: 'Casual Leave',
      NON_MENTIONABLE_COMPENSATORY_LEAVE: 'Compensatory Leave',
      NON_MENTIONABLE_PANDEMIC_LEAVE: 'Pandemic Leave',
      NON_MENTIONABLE_PATERNITY_LEAVE: 'Paternity Leave',
      NON_MENTIONABLE_MATERNITY_LEAVE: 'Maternity Leave',
      LEAVE: 'Leave',
      LEAVE_WITHOUT_PAY: 'LWP',
      LEAVE_WITHOUT_PAY_SANDWICH: 'LWP',
      GENERAL_HOLIDAY: 'General Holiday',
    };
    return object[status] ?? status;
  }
}
