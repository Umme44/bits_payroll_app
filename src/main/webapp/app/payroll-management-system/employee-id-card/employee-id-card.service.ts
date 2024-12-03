import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEmployeeStaticFile } from 'app/shared/model/employee-static-file.model';
import {createRequestOption} from "../../core/request/request-util";


type EntityResponseType = HttpResponse<IEmployeeStaticFile>;
type EntityArrayResponseType = HttpResponse<IEmployeeStaticFile[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeIdCardService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/employee-static-files';

  constructor(protected http: HttpClient) {}

  // create(employeeStaticFile: IEmployeeStaticFile): Observable<EntityResponseType> {
  //   return this.http.post<IEmployeeStaticFile>(this.resourceUrl, employeeStaticFile, { observe: 'response' });
  // }

  // update(employeeStaticFile: IEmployeeStaticFile): Observable<EntityResponseType> {
  //   return this.http.put<IEmployeeStaticFile>(this.resourceUrl, employeeStaticFile, { observe: 'response' });
  // }

  // find(id: number): Observable<EntityResponseType> {
  //   return this.http.get<IEmployeeStaticFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  // }

  // delete(id: number): Observable<HttpResponse<{}>> {
  //   return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  // }

  // uploadImage(employeeStaticFile: IEmployeeStaticFile, file: File): Observable<EntityResponseType> {
  //   const formData: FormData = new FormData();
  //   formData.append('file', file);
  //   const copy = this.convertDateFromClient(employeeStaticFile);
  //   formData.append('employeeStaticFile', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
  //   return this.http.put<IEmployeeStaticFile>(this.resourceUrl + '/id-card', formData, { observe: 'response' });
  // }

  protected convertDateFromClient(employeeStaticFile: IEmployeeStaticFile): IEmployeeStaticFile {
    const copy: IEmployeeStaticFile = Object.assign({}, employeeStaticFile, {
      id: employeeStaticFile.id,
    });
    return copy;
  }

  // getEmployeeStaticFileDTO(id: number): Observable<EntityResponseType> {
  //   return this.http.get<IEmployeeStaticFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  // }

  // uploadImageFiles(files: File[]): Observable<boolean> {
  //   const formData: FormData = new FormData();
  //   for (let i = 0; i < files.length; i++) {
  //     formData.append('file', files[i]);
  //   }
  //   return this.http.post<boolean>(this.resourceUrl + '/id-card', formData);
  // }

  // findIdCardList(req?: any): Observable<EntityArrayResponseType> {
  //   const options = createRequestOption(req);
  //   return this.http.get<IEmployeeStaticFile[]>(this.resourceUrl + '/id-card-list', { params: options, observe: 'response' });
  // }

  loadMyIDCard(): Observable<EntityResponseType> {
    return this.http.get<IEmployeeStaticFile>(SERVER_API_URL + 'api/common/my-id-card', { observe: 'response' });
  }
}
