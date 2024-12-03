import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IAttendanceSummary } from '../attendance-summary.model';
import { AttendanceSummaryFilterDto } from '../filter.model';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<IAttendanceSummary>;
type EntityArrayResponseType = HttpResponse<IAttendanceSummary[]>;

@Injectable({ providedIn: 'root' })
export class AttendanceSummarySearchService {
  public resourceUrl = SERVER_API_URL + 'api/attendance-mgt/attendance-summaries';

  constructor(private http: HttpClient) {}

  query(attendaceFilter: AttendanceSummaryFilterDto, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.post<IAttendanceSummary[]>(`${this.resourceUrl}/attendanceSummarySearch`, attendaceFilter, {
      params: options,
      observe: 'response',
    });
  }
}
