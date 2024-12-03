import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import dayjs from 'dayjs/esm';
import { map } from 'rxjs/operators';
import { IEmployeeImageUpload } from 'app/shared/model/employee-image-upload.model';
import { IEmployeeStaticFile } from '../../shared/model/employee-static-file.model';
import { createRequestOption } from '../../core/request/request-util';

type EntityResponseType = HttpResponse<IEmployeeStaticFile>;
type EntityArrayResponseType = HttpResponse<IEmployeeStaticFile[]>;

@Injectable({ providedIn: 'root' })
export class ImportEmployeeImageService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/import-image-upload';

  constructor(protected http: HttpClient) {}

  create(employeeStaticFile: IEmployeeStaticFile): Observable<EntityResponseType> {
    return this.http.post<IEmployeeStaticFile>(this.resourceUrl, employeeStaticFile, { observe: 'response' });
  }

  update(employeeStaticFile: IEmployeeStaticFile): Observable<EntityResponseType> {
    return this.http.put<IEmployeeStaticFile>(this.resourceUrl, employeeStaticFile, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeeStaticFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<HttpResponse<IEmployeeImageUpload[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployeeImageUpload[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employeeImageUpload: IEmployeeImageUpload) => {
        employeeImageUpload.dateOfJoining = employeeImageUpload.dateOfJoining ? dayjs(employeeImageUpload.dateOfJoining) : undefined;
      });
    }
    return res;
  }

  delete(pin: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${pin}`, { observe: 'response' });
  }

  uploadImageFiles(files: File[]): Observable<boolean> {
    const formData: FormData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('file', files[i]);
    }
    return this.http.post<boolean>(this.resourceUrl, formData);
  }

  findImageList(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployeeStaticFile[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  updateEmployeeImage(id: number, file: File[]): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file[0]);
    return this.http.put<IEmployeeStaticFile>(`${this.resourceUrl}/${id}`, formData, { observe: 'response' });
  }
}
