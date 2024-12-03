import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeStaticFile, NewEmployeeStaticFile } from '../employee-static-file.model';

export type PartialUpdateEmployeeStaticFile = Partial<IEmployeeStaticFile> & Pick<IEmployeeStaticFile, 'id'>;

export type EntityResponseType = HttpResponse<IEmployeeStaticFile>;
export type EntityArrayResponseType = HttpResponse<IEmployeeStaticFile[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeStaticFileService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/employee-static-files');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeStaticFile: NewEmployeeStaticFile): Observable<EntityResponseType> {
    return this.http.post<IEmployeeStaticFile>(this.resourceUrl, employeeStaticFile, { observe: 'response' });
  }

  update(employeeStaticFile: IEmployeeStaticFile): Observable<EntityResponseType> {
    return this.http.put<IEmployeeStaticFile>(
      `${this.resourceUrl}/${this.getEmployeeStaticFileIdentifier(employeeStaticFile)}`,
      employeeStaticFile,
      { observe: 'response' }
    );
  }

  partialUpdate(employeeStaticFile: PartialUpdateEmployeeStaticFile): Observable<EntityResponseType> {
    return this.http.patch<IEmployeeStaticFile>(
      `${this.resourceUrl}/${this.getEmployeeStaticFileIdentifier(employeeStaticFile)}`,
      employeeStaticFile,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeeStaticFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeeStaticFile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeStaticFileIdentifier(employeeStaticFile: Pick<IEmployeeStaticFile, 'id'>): number {
    return employeeStaticFile.id;
  }

  compareEmployeeStaticFile(o1: Pick<IEmployeeStaticFile, 'id'> | null, o2: Pick<IEmployeeStaticFile, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeStaticFileIdentifier(o1) === this.getEmployeeStaticFileIdentifier(o2) : o1 === o2;
  }

  addEmployeeStaticFileToCollectionIfMissing<Type extends Pick<IEmployeeStaticFile, 'id'>>(
    employeeStaticFileCollection: Type[],
    ...employeeStaticFilesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeStaticFiles: Type[] = employeeStaticFilesToCheck.filter(isPresent);
    if (employeeStaticFiles.length > 0) {
      const employeeStaticFileCollectionIdentifiers = employeeStaticFileCollection.map(
        employeeStaticFileItem => this.getEmployeeStaticFileIdentifier(employeeStaticFileItem)!
      );
      const employeeStaticFilesToAdd = employeeStaticFiles.filter(employeeStaticFileItem => {
        const employeeStaticFileIdentifier = this.getEmployeeStaticFileIdentifier(employeeStaticFileItem);
        if (employeeStaticFileCollectionIdentifiers.includes(employeeStaticFileIdentifier)) {
          return false;
        }
        employeeStaticFileCollectionIdentifiers.push(employeeStaticFileIdentifier);
        return true;
      });
      return [...employeeStaticFilesToAdd, ...employeeStaticFileCollection];
    }
    return employeeStaticFileCollection;
  }

  getEmployeeStaticFileDTO(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeeStaticFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  uploadImageFiles(files: File[]): Observable<boolean> {
    const formData: FormData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('file', files[i]);
    }
    return this.http.post<boolean>(this.resourceUrl + '/id-card', formData);
  }

  findIdCardList(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeeStaticFile[]>(this.resourceUrl + '/id-card-list', {
      params: options,
      observe: 'response',
    });
  }

  loadMyIDCard(): Observable<EntityResponseType> {
    return this.http.get<IEmployeeStaticFile>(this.applicationConfigService.getEndpointFor('api/common/my-id-card'), {
      observe: 'response',
    });
  }

  uploadImage(employeeStaticFile: IEmployeeStaticFile, file: File): Observable<EntityResponseType> {
    /* const formData: FormData = new FormData();
    const formData2: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(employeeStaticFile);
    formData2.append('employeeStaticFile', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<IEmployeeStaticFile>(this.resourceUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));*/

    /*return this.http
      .put<IEmployeeStaticFile>(this.resourceUrl, formData2,formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));*/

    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(employeeStaticFile);
    formData.append('employeeStaticFile', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http.put<IEmployeeStaticFile>(this.resourceUrl + '/id-card', formData, { observe: 'response' });
  }

  protected convertDateFromClient(employeeStaticFile: IEmployeeStaticFile): IEmployeeStaticFile {
    const copy: IEmployeeStaticFile = Object.assign({}, employeeStaticFile, {
      id: employeeStaticFile.id,
    });
    return copy;
  }
}
