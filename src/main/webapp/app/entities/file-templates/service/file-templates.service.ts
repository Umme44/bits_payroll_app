import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFileTemplates, NewFileTemplates } from '../file-templates.model';

export type PartialUpdateFileTemplates = Partial<IFileTemplates> & Pick<IFileTemplates, 'id'>;

export type EntityResponseType = HttpResponse<IFileTemplates>;
export type EntityArrayResponseType = HttpResponse<IFileTemplates[]>;

@Injectable({ providedIn: 'root' })
export class FileTemplatesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/file-templates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fileTemplates: NewFileTemplates): Observable<EntityResponseType> {
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

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileTemplates[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getFileTemplatesIdentifier(fileTemplates: Pick<IFileTemplates, 'id'>): number {
    return fileTemplates.id;
  }

  compareFileTemplates(o1: Pick<IFileTemplates, 'id'> | null, o2: Pick<IFileTemplates, 'id'> | null): boolean {
    return o1 && o2 ? this.getFileTemplatesIdentifier(o1) === this.getFileTemplatesIdentifier(o2) : o1 === o2;
  }

  addFileTemplatesToCollectionIfMissing<Type extends Pick<IFileTemplates, 'id'>>(
    fileTemplatesCollection: Type[],
    ...fileTemplatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fileTemplates: Type[] = fileTemplatesToCheck.filter(isPresent);
    if (fileTemplates.length > 0) {
      const fileTemplatesCollectionIdentifiers = fileTemplatesCollection.map(
        fileTemplatesItem => this.getFileTemplatesIdentifier(fileTemplatesItem)!
      );
      const fileTemplatesToAdd = fileTemplates.filter(fileTemplatesItem => {
        const fileTemplatesIdentifier = this.getFileTemplatesIdentifier(fileTemplatesItem);
        if (fileTemplatesCollectionIdentifiers.includes(fileTemplatesIdentifier)) {
          return false;
        }
        fileTemplatesCollectionIdentifiers.push(fileTemplatesIdentifier);
        return true;
      });
      return [...fileTemplatesToAdd, ...fileTemplatesCollection];
    }
    return fileTemplatesCollection;
  }
}
