import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IFileTemplates } from '../../shared/legacy/legacy-model/file-templates.model';
import { createRequestOption } from '../../core/request/request-util';

type EntityResponseType = HttpResponse<IFileTemplates>;
type EntityArrayResponseType = HttpResponse<IFileTemplates[]>;

@Injectable({ providedIn: 'root' })
export class FileTemplatesService {
  public resourceUrl = SERVER_API_URL + 'api/payroll-mgt/file-templates';

  public resourceUrlCommon = SERVER_API_URL + 'api/common/file-templates';

  constructor(protected http: HttpClient) {}

  create(fileTemplates: IFileTemplates): Observable<EntityResponseType> {
    return this.http.post<IFileTemplates>(this.resourceUrl, fileTemplates, { observe: 'response' });
  }

  createWithFile(file: File, fileTemplates: IFileTemplates): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('fileTemplates', new Blob([JSON.stringify(fileTemplates)], { type: 'application/json' }));
    return this.http.post<IFileTemplates>(this.resourceUrl, formData, { observe: 'response' });
  }

  updateWithoutFile(fileTemplates: IFileTemplates): Observable<EntityResponseType> {
    return this.http.put<IFileTemplates>(this.resourceUrl + '/without-file', fileTemplates, { observe: 'response' });
  }

  updateWithFile(file: File, fileTemplates: IFileTemplates): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('fileTemplates', new Blob([JSON.stringify(fileTemplates)], { type: 'application/json' }));
    return this.http.put<IFileTemplates>(this.resourceUrl, formData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFileTemplates>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  downloadAdmin(id: number): Observable<HttpResponse<Blob>> {
    const uri = this.resourceUrl + '/download/' + id;
    return this.http.get<Blob>(uri, { responseType: 'blob' as 'json', observe: 'response' });
  }

  queryAdminFileTemplates(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileTemplates[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAllTitlesForAdmin(): Observable<HttpResponse<string[]>> {
    return this.http.get<string[]>(this.resourceUrl + '/admin-suggestion-titles', { observe: 'response' });
  }

  getAllTitlesForUser(): Observable<HttpResponse<string[]>> {
    return this.http.get<string[]>(this.resourceUrlCommon + '/user-suggestion-titles', { observe: 'response' });
  }

  /// user service
  queryUserFileTemplates(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileTemplates[]>(SERVER_API_URL + 'api/common/file-templates', {
      params: options,
      observe: 'response',
    });
  }

  downloadFileCommonUser(id: number): Observable<HttpResponse<Blob>> {
    const uri = this.resourceUrlCommon + '/download/' + id;
    return this.http.get<Blob>(uri, { responseType: 'blob' as 'json', observe: 'response' });
  }
}
