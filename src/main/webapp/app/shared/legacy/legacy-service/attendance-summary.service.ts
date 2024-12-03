import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../../core/request/request-util';
import { IAttendanceSummary } from '../legacy-model/attendance-summary.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IAttendanceSummary>;
type EntityArrayResponseType = HttpResponse<IAttendanceSummary[]>;

@Injectable({ providedIn: 'root' })
export class AttendanceSummaryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/attendance-summaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(attendanceSummary: IAttendanceSummary): Observable<EntityResponseType> {
    return this.http.post<IAttendanceSummary>(this.resourceUrl, attendanceSummary, { observe: 'response' });
  }

  update(attendanceSummary: IAttendanceSummary): Observable<EntityResponseType> {
    return this.http.put<IAttendanceSummary>(this.resourceUrl, attendanceSummary, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAttendanceSummary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttendanceSummary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryWithParams(year: number, month: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttendanceSummary[]>(`${this.resourceUrl}/${year}/${month}`, {
      params: options,
      observe: 'response',
    });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  uploadXlsxFile(file: File, year: number, month: number): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.resourceUrl}/xlsx-upload/${year}/${month}`, formData, { observe: 'response' });
  }
}
