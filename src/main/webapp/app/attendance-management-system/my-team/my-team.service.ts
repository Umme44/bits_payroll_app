import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IAttendanceTimeSheet } from '../../shared/model/attendance-time-sheet.model';
import { ITimeRange } from '../../shared/model/time-range.model';
import { IMyTeamEmployee } from '../../shared/model/my-team-employee.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { createRequestOption } from '../../core/request/request-util';
import dayjs from 'dayjs/esm';

type EntityArrayResponseType = HttpResponse<IMyTeamEmployee[]>;

@Injectable({ providedIn: 'root' })
export class MyTeamService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/common/my-team-members-attendance-list');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMyTeamEmployee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateRangeClient(timeRange: ITimeRange): ITimeRange {
    const copy: ITimeRange = Object.assign({}, timeRange, {
      startDate: timeRange.startDate,
      endDate: timeRange.endDate,
    });
    return copy;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((myTeamEmployee: IMyTeamEmployee) => {
        myTeamEmployee.attendances?.forEach(attendanceEntry => {
          if (attendanceEntry) {
            attendanceEntry.date = attendanceEntry.date ? dayjs(attendanceEntry.date) : undefined;
          }
          if (attendanceEntry) {
            attendanceEntry.inTime = attendanceEntry.inTime ? dayjs(attendanceEntry.inTime) : undefined;
          }
          if (attendanceEntry) {
            attendanceEntry.outTime = attendanceEntry.outTime ? dayjs(attendanceEntry.outTime) : undefined;
          }
        });
      });
    }
    return res;
  }

  protected getCurrentDaysAttendanceOfCurrentEmployee(): Observable<HttpResponse<IAttendanceTimeSheet>> {
    return this.http.get<IAttendanceTimeSheet>(`${this.resourceUrl}/isExist`, { observe: 'response' });
  }
}
