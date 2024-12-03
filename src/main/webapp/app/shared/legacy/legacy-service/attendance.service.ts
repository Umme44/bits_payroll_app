import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IAttendance } from '../legacy-model/attendance.model';
import { createRequestOption } from '../../../core/request/request-util';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IAttendance>;
type EntityArrayResponseType = HttpResponse<IAttendance[]>;

@Injectable({ providedIn: 'root' })
export class AttendanceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attendances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(attendance: IAttendance): Observable<EntityResponseType> {
    return this.http.post<IAttendance>(this.resourceUrl, attendance, { observe: 'response' });
  }

  update(attendance: IAttendance): Observable<EntityResponseType> {
    return this.http.put<IAttendance>(this.resourceUrl, attendance, { observe: 'response' });
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

  getUserFriendlyAttendanceStatus(status: any): string {
    const object = {
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
    };
    return object[status] ?? status;
  }

  getUIFriendlyAttendanceStatusDashboard(status: any): string {
    const object = {
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
    };
    return object[status] ?? status;
  }
}
